package com.csdy.vampirismtinker.effect;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class ScarletFrenzy extends MobEffect {


    public ScarletFrenzy(){
        super(MobEffectCategory.HARMFUL,0x8B0000);
    }
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        entity.invulnerableTime =  0;
        entity.hurt(new DamageSource(Holder.direct(new DamageType("scarlet_frenzy", 0f))), 0.5F);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
