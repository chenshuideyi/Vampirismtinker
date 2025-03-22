package com.csdy.modifier;

import de.teamlapen.vampirism.entity.player.hunter.HunterPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

///TODO: 猎人等级加成,砸地板特效
public class CatholicVampireHunt extends NoLevelsModifier implements MeleeDamageModifierHook, MeleeHitModifierHook {
    @Override
    public float getMeleeDamage(IToolStackView iToolStackView, ModifierEntry modifierEntry, ToolAttackContext context, float baseDamage, float damage) {
        Player player = context.getPlayerAttacker();
        if (player == null) return 0;
        boolean message = !player.getCommandSenderWorld().isClientSide;
        HunterPlayer hunter = HunterPlayer.get(player);
        if (hunter.getLevel() < 1) {
            if (message) {
                player.displayClientMessage(Component.translatable("text.vampirism.can_not_be_used_faction"), true);
            }
            return 0;
        }
        return damage;
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity target = context.getLivingTarget();
        Player player = context.getPlayerAttacker();
        if (target != null && player != null && context.isCritical()) {
            jesusJudgment(player, (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE), 1.5, 4);
        }

    }

    private void jesusJudgment(Player player, float damage, double upwardSpeed, double range) {
        Level level = player.level();
        AABB area = new AABB(
                player.getX() - range, player.getY() - 1, player.getZ() - range,
                player.getX() + range, player.getY() + 1, player.getZ() + range
        );
        for (LivingEntity living : level.getEntitiesOfClass(LivingEntity.class, area)) {
            if (living != player) {
                living.hurt(level.damageSources().playerAttack(player), damage);
                living.setDeltaMovement(living.getDeltaMovement().add(0, upwardSpeed, 0));
            }
        }
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE);
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }
}
