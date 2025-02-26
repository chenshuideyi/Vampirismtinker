package com.csdy.frames.diadema;

import com.csdy.ModMain;
import com.csdy.frames.diadema.events.EntityEnteredDiademaEvent;
import com.csdy.frames.diadema.events.EntityExitedDiademaEvent;
import com.csdy.frames.diadema.movement.DiademaMovement;
import com.csdy.frames.diadema.movement.FollowDiademaMovement;
import com.csdy.frames.diadema.packets.DiademaCreatedPacket;
import com.csdy.frames.diadema.packets.DiademaRemovedPacket;
import com.csdy.frames.diadema.packets.DiademaUpdatePacket;
import com.csdy.frames.diadema.range.DiademaRange;
import lombok.Getter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;
import java.util.stream.Collectors;

/// 领域实例的抽象基类，实现这个和一旁的DiademaType还有DiademaClient即可实现自定义领域
@Mod.EventBusSubscriber(modid = ModMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public abstract class Diadema {
    // init&final
    private static long nextId = 0;

    public Diadema(DiademaType type, DiademaMovement movement) {
        this.type = type;
        this.movement = movement;
        this.instanceId = nextId++;

        // 实例上的事件处理器需要手动注册
        MinecraftForge.EVENT_BUS.register(this);
        //FMLJavaModLoadingContext.get().getModEventBus().register(this); //不是这个bus，注释掉了

        // 加到列表里
        type.instances.add(this);

        // 发包
        DiademaSyncing.CHANNEL.send(PacketDistributor.ALL.noArg(), new DiademaCreatedPacket(this.type, this.instanceId));
    }

    private void remove() {
        // 视作实体离开领域
        entities.forEach(this::removeEntity);

        // 同样实例在删除时候需要手动取消注册。不然因为注册也是个引用，可能导致内存泄漏或者null引用异常
        MinecraftForge.EVENT_BUS.unregister(this);

        // 从列表里移除
        type.instances.remove(this);

        // 发包
        DiademaSyncing.CHANNEL.send(PacketDistributor.ALL.noArg(), new DiademaRemovedPacket(this.instanceId));
    }


    // properties&methods
    @Getter
    private final DiademaType type;

    public boolean isOfType(DiademaType type) {
        return this.type == type;
    }

    final long instanceId;

    @Getter
    private ServerLevel level;

    @Getter
    private Vec3 position;

    public double getPosX() {
        return position.x;
    }

    public double getPosY() {
        return position.y;
    }

    public double getPosZ() {
        return position.z;
    }

    public abstract DiademaRange getRange();

    private final DiademaMovement movement;

    public Entity GetCore() {
        if (!(movement instanceof FollowDiademaMovement follow)) return null;
        return follow.getEntity();
    }

    public Player GetPlayer() {
        if (!(GetCore() instanceof Player player)) return null;
        return player;
    }

    private final Set<Entity> entities = new HashSet<>();

    /// 所有受这一领域影响的生物的视图，注意是只读的，进行更改会抛出异常。<br/>
    /// 发生变更后会有事件发出
    public final Set<Entity> affectingEntities = Collections.unmodifiableSet(entities);

    /// 终结这一领域
    public void kill() {
        remove();
    }


    // virtual methods

    /// 每帧执行一次的实例方法，放手一搏吧
    protected void perTick() {
    }

    /// 实体进入自己时会发生的实例方法，也许无需时刻监听事件
    protected void onEntityEnter(Entity entity) {
    }

    /// 实体离开自己时会发生的实例方法，值得一提的是这两个函数判定比事件早
    protected void onEntityExit(Entity entity) {
    }

    /// 重写这个来像Client同步自定义参数，把东西序列化到字节数组里，在另一边的ClientDiadema里还原回来即可
    protected byte[] getCustomSyncData() {
        return new byte[0];
    }


    // state updating
    private void updatePosition() {
        position = movement.getPosition();
        level = movement.getLevel();
    }

    private void removeEntity(Entity entity) {
        if (entities.remove(entity)) {
            type.removeAffected(entity);
            onEntityExit(entity);
            MinecraftForge.EVENT_BUS.post(new EntityExitedDiademaEvent(entity, this));
        }
    }

    private void addEntity(Entity entity) {
        if (entities.add(entity)) {
            type.addAffected(entity);
            onEntityEnter(entity);
            MinecraftForge.EVENT_BUS.post(new EntityEnteredDiademaEvent(entity, this));
        }
    }

    private void updateEntities() {
        var inRange = getRange().getAffectingEntities().collect(Collectors.toSet());
        entities.stream().filter(entity -> !inRange.contains(entity)).toList().forEach(this::removeEntity);
        inRange.forEach(this::addEntity);
    }


    // event handlers
    @SubscribeEvent
    public final void onServerTick(TickEvent.ServerTickEvent e) {
        updatePosition();
        updateEntities();
        perTick();
        // 发包
        DiademaSyncing.CHANNEL.send(
                PacketDistributor.ALL.noArg(),
                new DiademaUpdatePacket(this.instanceId, level.dimension().location(), position, getCustomSyncData())
        );
    }

    @SubscribeEvent
    public final void onServerStopped(ServerStoppedEvent e) {
        this.remove();
    }

    @SubscribeEvent
    public final void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent e) {
        // 发包
        DiademaSyncing.CHANNEL.send(
                PacketDistributor.PLAYER.with(() -> (ServerPlayer) e.getEntity()),
                new DiademaCreatedPacket(type, instanceId));
    }
}
