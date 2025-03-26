package com.csdy.vampirismtinker.mixins;

import com.csdy.vampirismtinker.effect.EffectsRegister;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingHealthMixin {

    @Inject(
            method = "heal",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onHeal(float p_21116_, CallbackInfo cir) {
        LivingEntity entity = (LivingEntity)(Object)this;
        if (entity.hasEffect(EffectsRegister.SILVER_FLAME_BRAND.get())) {
            entity.playSound(SoundEvents.SKELETON_HURT, 0.5F, 1.5F);
            cir.cancel();
        }
    }
}
