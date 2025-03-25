package com.csdy.vampirismtinker.effect;

import com.csdy.vampirismtinker.ModMain;
import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
@Mod.EventBusSubscriber(modid = ModMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BloodcurseErosion extends MobEffect {
    private static final String SPEED_MODIFIER_UUID = "7107DE5E-7CE8-4030-940E-514C1F160891"; // 唯一标识符
    private static final double SPEED_REDUCTION = -0.5;

    private static final String ATTACK_DAMAGE_MODIFIER_UUID = "7107DE5E-7CE8-4030-940E-514C1F160891";
    private static final double ATTACK_DAMAGE_REDUCTION = -0.5;


    public BloodcurseErosion() {
        super(MobEffectCategory.HARMFUL, 0);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, SPEED_MODIFIER_UUID, SPEED_REDUCTION, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_MODIFIER_UUID, ATTACK_DAMAGE_REDUCTION, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        float amount = event.getAmount();
        if (entity.hasEffect(EffectsRegister.BLOOD_CURSE_EROSION.get())) {
            entity.hurt(new DamageSource(Holder.direct(new DamageType("blood_curse", 0f))), -Math.abs(amount) * 2);
        }
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
        entity.hurt(new DamageSource(Holder.direct(new DamageType("blood_curse", 0f))), 66.6F);
    }
}
