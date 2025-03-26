package com.csdy.vampirismtinker.modifier.vampire;

import com.csdy.vampirismtinker.effect.EffectsRegister;
import com.csdy.vampirismtinker.modifier.VampireBaseModifer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import static com.csdy.vampirismtinker.modifier.method.ModifierUtil.forceAddEffect;

public class ScarletFrenzy extends VampireBaseModifer implements MeleeHitModifierHook {

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry entry, ToolAttackContext context, float damageDealt) {
        LivingEntity target = context.getLivingTarget();
        Player player = context.getPlayerAttacker();
        if (target != null && player != null) {
            forceAddEffect(target, new MobEffectInstance(EffectsRegister.SCARLET_FRENZY.get(), (int) (10 * vampireLevelCorrection(player) * entry.getLevel()),0));//数值需要考虑
        }

    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this,ModifierHooks.MELEE_HIT);
        super.registerHooks(hookBuilder);
    }
}
