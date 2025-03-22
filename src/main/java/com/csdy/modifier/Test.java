package com.csdy.modifier;

import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.util.Helper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import javax.annotation.Nonnull;

import static com.google.common.primitives.Floats.max;

public class Test extends NoLevelsModifier implements MeleeHitModifierHook, MeleeDamageModifierHook {

//    @Override
//    public void onEquip(@NotNull IToolStackView tool, @NotNull ModifierEntry entry, EquipmentChangeContext context) {
//        LivingEntity entity = context.getEntity();
//        if (!(entity instanceof Player player)) return;
//        VampirePlayer vampire = VampirePlayer.get(player);
//        if (vampire.getLevel() < 1) {
//            System.out.println(111111111);
//        }
//    }


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

    private void dropItem(Player player, ItemStack stack) {
        if (!player.level().isClientSide) { // 确保只在服务端执行
            // 从玩家手中移除物品
            player.getInventory().removeItem(stack);

            // 在玩家周围生成掉落物
            ItemEntity itemEntity = new ItemEntity(
                    player.level(),
                    player.getX(), player.getY(), player.getZ(), // 掉落物的位置
                    stack.copy() // 掉落物的堆栈
            );
            itemEntity.setPickUpDelay(40); // 设置拾取延迟
            player.level().addFreshEntity(itemEntity); // 将掉落物添加到世界中
        }
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE);
    }

    @Override
    public float getMeleeDamage(IToolStackView iToolStackView, ModifierEntry modifierEntry, ToolAttackContext context, float baseDamage, float damage) {
        Player player = context.getPlayerAttacker();
        if (player == null) return 0;
        boolean message = !player.getCommandSenderWorld().isClientSide;
        VampirePlayer vampire = VampirePlayer.get(player);
        if (vampire.getLevel() < 1) {
            if (message) {
                player.displayClientMessage(Component.translatable("text.vampirism.can_not_be_used_faction"), true);
            }
            return 0;
        }
        return damage;
    }
}
