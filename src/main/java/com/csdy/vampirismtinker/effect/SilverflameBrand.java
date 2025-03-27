package com.csdy.vampirismtinker.effect;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import org.jetbrains.annotations.NotNull;

public class SilverflameBrand extends MobEffect {


    private static final String SPEED_MODIFIER_UUID = "7107DE5E-7CE8-4030-940E-514C1F160891"; // 唯一标识符
    private static final double SPEED_REDUCTION = -0.5;

    private static final String ATTACK_DAMAGE_MODIFIER_UUID = "7107DE5E-7CE8-4030-940E-514C1F160891";
    private static final double ATTACK_DAMAGE_REDUCTION = -0.5;


    public SilverflameBrand() {
        super(MobEffectCategory.HARMFUL, 0);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, SPEED_MODIFIER_UUID, SPEED_REDUCTION, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_MODIFIER_UUID, ATTACK_DAMAGE_REDUCTION, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        entity.hurt(entity.damageSources().magic(), 7.0F);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
        entity.hurt(new DamageSource(Holder.direct(new DamageType("hallelu_yah", 0f))), 77F);
    }
}
