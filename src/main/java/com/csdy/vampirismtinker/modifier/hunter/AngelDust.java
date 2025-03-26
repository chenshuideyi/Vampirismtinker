package com.csdy.vampirismtinker.modifier.hunter;

import com.csdy.vampirismtinker.modifier.HunterBaseModifer;
import de.teamlapen.vampirism.util.DamageHandler;
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
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import javax.annotation.Nullable;

import static com.csdy.vampirismtinker.modifier.method.ModifierUtil.reflectionPenetratingDamage;
import static java.lang.Math.ceil;

public class AngelDust extends HunterBaseModifer implements MeleeHitModifierHook, MeleeDamageModifierHook, ProjectileHitModifierHook {

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry entry, ToolAttackContext context, float damageDealt) {
        LivingEntity target = context.getLivingTarget();
        Player player = context.getPlayerAttacker();
        if (target != null && player != null && isVampireOrUndead(target)) {
            float value = ToolStats.ATTACK_DAMAGE.getDefaultValue() * hunterPower * 1.5F*entry.getLevel();
            reflectionPenetratingDamage(target,player,value/10);//数值需要考虑
        }

    }

    @Override
    public boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT data, ModifierEntry entry, Projectile projectile, EntityHitResult hit, @Nullable LivingEntity attacker, @Nullable LivingEntity target) {
        if (projectile instanceof AbstractArrow arrow && target != null) {
            if (!(attacker instanceof  Player player)) return false;
            if (!isVampireOrUndead(target)) return false;
            float value = (float) (Math.ceil(ToolStats.VELOCITY.getDefaultValue() * ToolStats.PROJECTILE_DAMAGE.getDefaultValue()) * hunterPower * 1.5*entry.getLevel());
            reflectionPenetratingDamage(target,player,value/50);//数值需要考虑
            target.setSecondsOnFire(10);
            arrow.setRemoved(Entity.RemovalReason.KILLED);
        }
        return false;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
        hookBuilder.addHook(this, ModifierHooks.PROJECTILE_HIT);
    }

}
