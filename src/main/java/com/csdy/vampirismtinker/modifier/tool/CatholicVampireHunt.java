package com.csdy.vampirismtinker.modifier.tool;

import com.csdy.vampirismtinker.modifier.HunterBaseModifer;
import de.teamlapen.vampirism.entity.player.hunter.HunterPlayer;
import de.teamlapen.vampirism.entity.vampire.VampireBaseEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import static com.csdy.vampirismtinker.modifier.method.ModifierUtil.hunterPower;


///TODO: 封装过于搞笑,必须重构
/// 我们安全了,暂时
/// 那么,继续简化吧
public class CatholicVampireHunt extends HunterBaseModifer implements MeleeDamageModifierHook, MeleeHitModifierHook {
    @Override
    public float getMeleeDamage(IToolStackView iToolStackView, ModifierEntry modifierEntry, ToolAttackContext context, float baseDamage, float damage) {
        float originalDamage = super.getMeleeDamage(iToolStackView, modifierEntry, context, baseDamage, damage);
        if (originalDamage == 0) return 0;
        Player player = context.getPlayerAttacker();
        if (player == null) return 0;
        return originalDamage * hunterLevelCorrection(player);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity target = context.getLivingTarget();
        Player player = context.getPlayerAttacker();
        if (target != null && player != null && context.isCritical()) {
            int hunterLevel =hunterPlayerLevel(player);
            double range =  divideAndRoundUp(hunterLevel);
            if (isVampireOrUndead(target)) jesusJudgment(player, (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) * hunterLevelCorrection(player) * 10f, range);
            else jesusJudgment(player, (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) * hunterLevelCorrection(player), range);
        }

    }

    private void jesusJudgment(Player player, float damage, double range) {
        Level level = player.level();
        int hunterlevel = hunterPlayerLevel(player);
        AABB area = new AABB(
                player.getX() - range, player.getY() - range, player.getZ() - range,
                player.getX() + range, player.getY() + range, player.getZ() + range
        );
        for (LivingEntity living : level.getEntitiesOfClass(LivingEntity.class, area)) {
            if (living != player) {
                living.hurt(level.damageSources().playerAttack(player), damage);
                repelEntity(living,player,range,divideAndRoundUp(hunterlevel));
            }
        }
    }

    public static int divideAndRoundUp(int number) {
        int result = number / 2;
        if (number % 2 != 0) {
            result += 1; // 有余数加1
        }
        return result;
    }

    private void repelEntity(Entity entity, Player player, double range,double hunterlevel) {
        // 获取玩家和实体的位置
        Vec3 playerPos = player.position();
        Vec3 entityPos = entity.position();

        // 计算距离
        double distanceSquared = entityPos.distanceToSqr(playerPos);
        double radiusSquared = range * range;

        // 计算斥力强度（1 - (距离平方 / 领域半径平方)）
        double repelStrength = 1 - (distanceSquared / radiusSquared);

        // 确保斥力强度在 [0, 1] 范围内
        repelStrength = Math.max(0, Math.min(1, repelStrength));

        // 计算排斥方向（从玩家指向实体）
        Vec3 repelDirection = entityPos.subtract(playerPos).normalize();

        // 设置排斥速度
        // 排斥强度，可以调整
        Vec3 repelVelocity = repelDirection.scale(hunterlevel * repelStrength);

        // 添加向上的力
        // 向上的力强度，可以调整
        Vec3 upwardVelocity = new Vec3(0, hunterlevel * repelStrength, 0); // 纯向上的力

        // 合并排斥速度和向上的力
        Vec3 finalVelocity = repelVelocity.add(upwardVelocity);

        // 为实体设置速度
        entity.setDeltaMovement(finalVelocity);
    }


    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE);
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }
}
