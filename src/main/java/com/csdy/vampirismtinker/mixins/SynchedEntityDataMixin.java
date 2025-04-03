package com.csdy.vampirismtinker.mixins;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = SynchedEntityData.class, priority = 1)
public abstract class SynchedEntityDataMixin {
    @Final
    @Shadow
    private Entity entity;

    @Shadow public boolean isDirty;

    /**
     * 拦截 set 方法：当外部逻辑试图修改 DATA_HEALTH_ID 时
     * (1) 如果实体是 TheLastEndEntity，则直接取消所有外部写入；
     * (2) 对于其它 LivingEntity，如果存在绝对毁灭伤害记录，
     *     则将写入的健康值限定为 Math.min(外部值, 绝对毁灭计算后的健康值)。
     */
//    @Inject(
//        method = "set(Lnet/minecraft/network/syncher/EntityDataAccessor;Ljava/lang/Object;)V",
//        at = @At("HEAD"),
//        cancellable = true
//    )
//    public <T> void onSet(EntityDataAccessor<T> key, T value, CallbackInfo ci) {
//        // 仅处理血量字段
//        if (key == LivingEntity.DATA_HEALTH_ID && this.entity instanceof LivingEntity living) {
//                if (living instanceof Player) return;
//                if (value instanceof Float incomingHealth) {
//                    float forcedHealth = Math.min(incomingHealth, 0);
//                    living.getEntityData().set((EntityDataAccessor<Float>) key, forcedHealth);
//                    ci.cancel();
//                    }
//                }
//
//        }


    /**
     * 拦截 get 方法：当读取 DATA_HEALTH_ID 时，
     * (1) 对 TheLastEndEntity，返回由 TheEndEntityHealthManager 管理的血量；
     * (2) 对其他实体，如果存在绝对毁灭伤害记录，则返回实际血量与毁灭伤害计算值中的较小值，
     *     以确保外界看到的血量不高于绝对毁灭伤害预期。
     */
//    @Inject(
//        method = "get(Lnet/minecraft/network/syncher/EntityDataAccessor;)Ljava/lang/Object;",
//        at = @At("HEAD"),
//        cancellable = true
//    )
//    public <T> void onGet(EntityDataAccessor<T> key, CallbackInfoReturnable<T> cir) {
//        if (key == LivingEntity.DATA_HEALTH_ID && this.entity instanceof LivingEntity living) {
//            if (living instanceof Player) return;
//                float actual = living.getHealth();
//                float forcedView = Math.min(actual, 0);
//                cir.setReturnValue((T)(Object)forcedView);
//        }
//    }
}
