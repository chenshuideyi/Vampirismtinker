package com.csdy.modifier;

import com.csdy.modifier.method.ModifierUntil;
import de.teamlapen.vampirism.api.VampirismAPI;
import de.teamlapen.vampirism.entity.player.hunter.HunterPlayer;
import de.teamlapen.vampirism.entity.vampire.VampireBaseEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
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

///TODO: 封装过于搞笑,必须重构
/// 我们安全了,暂时
public class CatholicVampireHunt extends NoLevelsModifier implements MeleeDamageModifierHook, MeleeHitModifierHook {
    private static final float hunterPower = 0.2f;
    @Override
    public float getMeleeDamage(IToolStackView iToolStackView, ModifierEntry modifierEntry, ToolAttackContext context, float baseDamage, float damage) {
        Player player = context.getPlayerAttacker();
        if (player == null) return 0;
        boolean message = !player.getCommandSenderWorld().isClientSide;
        int hunterLevel = hunterPlayerLevel(player);
        if (hunterLevel < 1) {
            if (message) {
                player.displayClientMessage(Component.translatable("text.vampirism.can_not_be_used_faction"), true);
            }
            return 0;
        }
        return damage * hunterLevel * hunterPower;
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity target = context.getLivingTarget();
        Player player = context.getPlayerAttacker();
        if (target != null && player != null && context.isCritical()) {
            int hunterLevel =hunterPlayerLevel(player);
            double range =  divideAndRoundUp(hunterLevel);
            if (isVampire(target)) jesusJudgment(player, (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) * hunterLevel * hunterPower * 10f, range);
            else jesusJudgment(player, (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE) * hunterLevel * hunterPower, range);
            ///我不他明白这是怎么运作的
//        Level level = target.level();
//        int intrange =  divideAndRoundUp(hunterLevel);
//        if (level instanceof ServerLevel serverLevel) {
//            ModifierUntil.spawnFallingBlockByPos(serverLevel, player.getOnPos(), range, range);
//            ModifierUntil.spawnFallingBlockByPos(serverLevel, player.getOnPos(), (float) range);
//            ModifierUntil.advancedBreakBlocks(level, player, (float) range, intrange, intrange, intrange, intrange, (float) range, true, true);
//        }
        }

    }

    private int hunterPlayerLevel(Player player){
        HunterPlayer hunter = HunterPlayer.get(player);
        return hunter.getLevel();
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

    public static boolean isVampire(LivingEntity entity) {
        return entity instanceof VampireBaseEntity;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE);
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }
}
