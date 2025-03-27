package com.csdy.vampirismtinker.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class Judgment extends MobEffect {
    /**
     * 具体实现:
     *
     * @see com.csdy.vampirismtinker.mixins.LivingHealthMixin
     */
    public Judgment() {
        super(MobEffectCategory.HARMFUL, 0);
    }
}
