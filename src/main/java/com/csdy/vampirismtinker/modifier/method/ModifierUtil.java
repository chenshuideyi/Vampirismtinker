package com.csdy.vampirismtinker.modifier.method;

import com.csdy.vampirismtinker.modifier.VampireBaseModifer;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.Modifier;

import java.lang.reflect.Field;
import java.util.Map;

import static com.csdy.vampirismtinker.ModMain.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModifierUtil {

    //DATA_HEALTH_ID
    private static final EntityDataAccessor<Float> DATA_HEALTH_ID = getHealthDataAccessor();

    private static EntityDataAccessor<Float> getHealthDataAccessor() {
        ///因为mc的狗屎混淆所以DATA_HEALTH_ID得写成f_20961_,不然非开发环境会报错,密码个逼
        ///普猫无视这个,因此AOP贯穿是必要的
        try {
//            Field field = LivingEntity.class.getDeclaredField("DATA_HEALTH_ID");
            Field field = LivingEntity.class.getDeclaredField("f_20961_");
            field.setAccessible(true);
            Object value = field.get(null);

            if (value instanceof EntityDataAccessor) {
                return (EntityDataAccessor<Float>) value;
            } else {
                System.err.println("DATA_HEALTH_ID is not of type EntityDataAccessor");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    ///反射贯穿伤害<br/>
    ///target 目标<br/>
    ///player 玩家<br/>
    ///float 造成的伤害<br/>
    ///  打穿王八壳,放手一搏吧<br/>
    /// 不是永远都有用,,,,,
    public static void reflectionPenetratingDamage(Entity target, Player player, float value) {
        if (!(target instanceof LivingEntity living)) return;
        if (DATA_HEALTH_ID == null) return;
        float currentHealth = living.getEntityData().get(DATA_HEALTH_ID);
        float newHealth = currentHealth-value;
        living.getEntityData().set(DATA_HEALTH_ID, newHealth);
        if (living.getHealth() <= 0.0F) {
            living.die(living.level().damageSources().playerAttack(player));
        }
    }

    ///反射设置生命值<br/>
    ///target 目标<br/>
    ///player 玩家<br/>
    ///float 设置的值<br/>
    /// 打穿王八壳,放手一搏吧<br/>
    /// 不是永远都有用,,,,,
    public static void reflectionSetHealth(Entity target, Player player, float value) {
        if (!(target instanceof LivingEntity living)) return;
        if (DATA_HEALTH_ID == null) return;
        living.getEntityData().set(DATA_HEALTH_ID, value);
        if (living.getHealth() <= 0.0F) {
            living.die(living.level().damageSources().playerAttack(player));
        }
    }


    /**
     * 强制添加效果实例（包括自定义效果）
     * @param entity 目标生物
     * @param instance 效果实例（包含效果类型、时长、等级等）
     */
    public static void forceAddEffect(LivingEntity entity, MobEffectInstance instance) {
        try {
            // 获取效果Map字段（根据mapping调整字段名）
            ///f_20945_
            Field effectsField = LivingEntity.class.getDeclaredField("f_20945_");
//            Field effectsField = LivingEntity.class.getDeclaredField("activeEffects");
            effectsField.setAccessible(true);

            @SuppressWarnings("unchecked")
            Map<MobEffect, MobEffectInstance> effects = (Map<MobEffect, MobEffectInstance>) effectsField.get(entity);
            effects.put(instance.getEffect(), instance);

            if (!entity.level().isClientSide) {
                entity.getAttributes().assignValues(entity.getAttributes()); // 刷新属性
                if (entity instanceof ServerPlayer player) player.connection.send(new ClientboundUpdateMobEffectPacket(entity.getId(), instance));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
