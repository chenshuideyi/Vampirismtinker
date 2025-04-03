package com.csdy.vampirismtinker.modifier.method;

import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

import java.lang.reflect.Field;
import java.util.Map;

public class HealthReflector {
    private static final EntityDataAccessor<Float> DATA_HEALTH_ID;
    private static final Field DATA_ITEM_FIELD;

    static {
        try {
            // 1. 获取DATA_HEALTH_ID字段（1.20.1混淆名：f_20948_）
            Field healthField = LivingEntity.class.getDeclaredField("DATA_HEALTH_ID");
            healthField.setAccessible(true);
            DATA_HEALTH_ID = (EntityDataAccessor<Float>) healthField.get(null);

            // 2. 获取SynchedEntityData.items的私有字段
            DATA_ITEM_FIELD = SynchedEntityData.class.getDeclaredField("itemsById"); // items
            DATA_ITEM_FIELD.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException("初始化反射字段失败", e);
        }
    }

    /**
     * 强制设置实体血量（绕过所有检查）
     */
    public static void unsafeSetHealth(LivingEntity entity, float health) {
        try {
            // 1. 获取SynchedEntityData实例
            SynchedEntityData data = entity.getEntityData();

            // 2. 获取内部DataItem映射（Map<EntityDataAccessor<?>, DataItem<?>>）
            @SuppressWarnings("unchecked")
            Map<EntityDataAccessor<?>, SynchedEntityData.DataItem<?>> items =
                    (Map<EntityDataAccessor<?>, SynchedEntityData.DataItem<?>>) DATA_ITEM_FIELD.get(data);

            // 3. 直接修改DataItem中的值
            SynchedEntityData.DataItem<?> item = items.get(DATA_HEALTH_ID);
            if (item != null) {
                ((SynchedEntityData.DataItem<Float>) item).setValue(health);
                data.isDirty(); // 标记为需要同步
            }

            // 4. 强制同步到客户端
            if (!entity.level().isClientSide && entity instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(
                        new ClientboundSetHealthPacket(
                                health,
                                serverPlayer.getFoodData().getFoodLevel(),
                                serverPlayer.getFoodData().getSaturationLevel()
                        )
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("修改血量失败", e);
        }
    }
}
