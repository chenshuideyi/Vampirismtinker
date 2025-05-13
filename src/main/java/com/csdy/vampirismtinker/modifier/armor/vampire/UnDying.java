package com.csdy.vampirismtinker.modifier.armor.vampire;

import com.csdy.vampirismtinker.modifier.VampireBaseModifer;
import com.csdy.vampirismtinker.modifier.register.ModifierRegister;
import de.teamlapen.vampirism.core.ModEffects;
import de.teamlapen.vampirism.core.ModEntities;
import de.teamlapen.vampirism.core.ModSounds;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.helper.ModifierUtil;

import static com.csdy.vampirismtinker.ModMain.MODID;
import static com.csdy.vampirismtinker.modifier.method.ModifierUtil.forceAddEffect;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class UnDying extends VampireBaseModifer {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void LivingDeathVampire(LivingDeathEvent event) {
        //吸血鬼护甲免死相关
        if (event.getEntity() instanceof Player player) {
            if (isTakingSundamage(player)) return;
            MobEffectInstance effect = player.getActiveEffectsMap().get(ModEffects.ARMOR_REGENERATION.get());
            if (effect != null && effect.getDuration() > 0) return;
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (slot.getType() != EquipmentSlot.Type.ARMOR) continue;
                ItemStack stack = player.getItemBySlot(slot);
                if (ModifierUtil.getModifierLevel(stack, ModifierRegister.UN_DYING_STATIC_MODIFIER.getId()) > 0) {
                    playTotemEffects(player);
                    int vampireLevel = VampirePlayer.get(player).getLevel();
                    int durationTicks;
                    if (vampireLevel >= 14) {
                        durationTicks = 1320; // 14级固定66秒
                    } else {
                        durationTicks = 3600 - (vampireLevel - 1) * 190; // 每级减190t
                    }
                    blinding(player,3);
                    forceAddEffect(player, (new MobEffectInstance(ModEffects.ARMOR_REGENERATION.get(), durationTicks,0)));
                    for (int i = 0; i < vampireLevel; ++i) {
                        summonBatAroundPlayer(player);
                    }
                    spawnSmokeParticles(player,6666);
                    player.setHealth(player.getMaxHealth()/2);
                    player.deathTime = -2;
                    player.isAlive();
                    player.invulnerableTime = 60;
                    event.setCanceled(true);
                }
            }
        }
    }



    private static void summonBatAroundPlayer(Player player) {
        if (!player.level().isClientSide) {
            ServerLevel serverLevel = (ServerLevel) player.level();

            double x = player.getX() + (player.getRandom().nextDouble() - 0.5) * 4;
            double y = player.getY() + 1;
            double z = player.getZ() + (player.getRandom().nextDouble() - 0.5) * 4;

            // 创建蝙蝠实体
            Bat bat = EntityType.BAT.create(serverLevel);
            if (bat != null) {
                bat.moveTo(x, y, z, player.getRandom().nextFloat() * 360, 0);
                bat.setNoAi(false);
                serverLevel.addFreshEntity(bat);
            }
        }
    }

    private static void spawnSmokeParticles(Player player, int count) {
        //令神龙制造烟雾
        if (player.level() instanceof ServerLevel serverLevel) {
            Vec3 pos = player.position();
            serverLevel.sendParticles(
                    ParticleTypes.SMOKE,
                    pos.x, pos.y + 1.0, pos.z,
                    count,
                    1.7, 1.7, 1.7,
                    0.1
            );
        }
    }

    private static void blinding(Player player,double range) {
        Level level = player.level();
        AABB area = new AABB(
                player.getX() - range, player.getY() - range, player.getZ() - range,
                player.getX() + range, player.getY() + range, player.getZ() + range
        );
        for (LivingEntity living : level.getEntitiesOfClass(LivingEntity.class, area)) {
            if (living != player) forceAddEffect(living, (new MobEffectInstance(MobEffects.BLINDNESS, 120,0)));

        }
    }

    private static void playTotemEffects(Player player) {
        if (!player.level().isClientSide) {
            ServerLevel level = (ServerLevel) player.level();

            // 播放音效
            level.playSound(
                            null,
                            player.getX(), player.getY(), player.getZ(),
                            SoundEvents.TOTEM_USE,
                            SoundSource.PLAYERS,
                            1.0F,
                            1.0F
                    );

            // 生成粒子效果
            level.sendParticles(
                            ParticleTypes.TOTEM_OF_UNDYING,
                            player.getX(), player.getY() + 1.0, player.getZ(),
                            100, // 粒子数量
                            0.5, 0.5, 0.5,
                            0.2
                    );
        }
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }
}
