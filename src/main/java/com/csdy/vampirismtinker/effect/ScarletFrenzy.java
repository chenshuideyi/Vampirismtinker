package com.csdy.vampirismtinker.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;

public class ScarletFrenzy extends MobEffect {
    /**
     * 具体实现:
     *
     * @see com.csdy.vampirismtinker.mixins.LivingHealthMixin
     */
    public ScarletFrenzy(){
        super(MobEffectCategory.HARMFUL,0x8B0000);
    }

}
