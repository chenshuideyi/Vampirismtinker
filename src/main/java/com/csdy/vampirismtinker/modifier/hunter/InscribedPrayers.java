package com.csdy.vampirismtinker.modifier.hunter;

import com.csdy.vampirismtinker.modifier.HunterBaseModifer;
import de.teamlapen.vampirism.util.DamageHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import javax.annotation.Nullable;


public class InscribedPrayers extends HunterBaseModifer implements MeleeHitModifierHook, MeleeDamageModifierHook, ProjectileHitModifierHook {

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry entry, ToolAttackContext context, float baseDamage, float damage) {
        float originalDamage = super.getMeleeDamage(tool, entry, context, baseDamage, damage);
        if (originalDamage == 0) return 0;
        Player player = context.getPlayerAttacker();
        if (player == null) return 0;
        LivingEntity target = context.getLivingTarget();
        if (target == null) return 0;
        if (isVampireOrUndead(target)){
            target.setSecondsOnFire(10);
            return originalDamage * hunterLevelCorrection(player) * (1 + entry.getLevel() * 0.2f);
        }
        return originalDamage;
    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT data, ModifierEntry entry, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (projectile instanceof AbstractArrow arrow && target != null) {
            if (!(attacker instanceof  Player player)) return false;
            if (!isVampireOrUndead(target)) return false;
            target.setSecondsOnFire(10);
            arrow.setBaseDamage(arrow.getBaseDamage() * hunterLevelCorrection(player) * (1 + entry.getLevel() * 0.2f));
            arrow.setRemoved(Entity.RemovalReason.KILLED);
        }
        return false;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE);
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
        hookBuilder.addHook(this, ModifierHooks.PROJECTILE_HIT);
    }

}
