package com.csdy.vampirismtinker.modifier.armor.hunter;

import com.csdy.vampirismtinker.effect.EffectsRegister;
import com.csdy.vampirismtinker.modifier.HunterBaseModifer;
import de.teamlapen.vampirism.entity.vampire.VampireBaseEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import static com.csdy.vampirismtinker.modifier.method.ModifierUtil.*;
import static com.csdy.vampirismtinker.modifier.method.ModifierUtil.forceAddEffect;

public class HolyRomanRebornWarrior extends HunterBaseModifer implements ModifyDamageModifierHook,OnAttackedModifierHook, InventoryTickModifierHook {
    private static final float TARGET_REDUCTION = 0.77f; // 77%
    private static final int TRIGGER_COUNT = 4;

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void onAttacked(IToolStackView tool, ModifierEntry entry, EquipmentContext context, EquipmentSlot slot, DamageSource damageSource, float amount, boolean isDirectDamage) {
        Entity attacker = damageSource.getEntity();
        if (!(context.getEntity() instanceof Player player)) return;
        if (!(attacker instanceof LivingEntity living)) return;
        float value = 777 * hunterLevelCorrection(player) * 0.01F;//777乘百分之一猎人等级修正
        if (isVampireOrUndead(living)) reflectionPenetratingDamage(living,player,value);
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.ON_ATTACKED);
        hookBuilder.addHook(this, ModifierHooks.MODIFY_DAMAGE);
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK);
        super.registerHooks(hookBuilder);
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry entry, EquipmentContext context, EquipmentSlot slot, DamageSource damageSource, float amount, boolean isDirectDamage) {
        if (!(context.getEntity() instanceof Player player)) return amount;
        float correction = hunterLevelCorrection(player);
        float singleReduction = (float) Math.pow(TARGET_REDUCTION, 1.0/TRIGGER_COUNT);
        float damageMultiplier = 1 - (1 - singleReduction) * (correction / 2.8f);
        return amount * damageMultiplier;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry entry, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (!(holder instanceof Player player)) return;
        if (isCorrectSlot && hunterPlayerLevel(player) > 0) {
            if (!(player.getHealth() < player.getMaxHealth())) return;
            if (player.tickCount % 20 == 0) {
                if (player.getFoodData().getFoodLevel() > 18)
                     player.heal(3 * hunterPlayerLevel(player) * hunterLevelCorrection(player)); // 直接计算
                else player.heal(1 * hunterPlayerLevel(player) * hunterLevelCorrection(player)); // 基础恢复
            }
        }
        super.onInventoryTick(tool,entry,world,holder,itemSlot,isSelected,isCorrectSlot,stack);
    }
}
