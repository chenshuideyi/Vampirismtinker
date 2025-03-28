package com.csdy.vampirismtinker.modifier.armor.hunter;

import com.csdy.vampirismtinker.effect.EffectsRegister;
import com.csdy.vampirismtinker.modifier.HunterBaseModifer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.ArmorLootingModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import static com.csdy.vampirismtinker.modifier.method.ModifierUtil.*;

public class InscribedPrayersArmor extends HunterBaseModifer implements ModifyDamageModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MODIFY_DAMAGE);
        super.registerHooks(hookBuilder);
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry entry, EquipmentContext context, EquipmentSlot slot, DamageSource damageSource, float amount, boolean isDirectDamage) {
        Entity entity = damageSource.getEntity();
        Entity directEntity = damageSource.getDirectEntity();
        if (!(context.getEntity() instanceof Player player)) return amount;
        if (!(entity instanceof LivingEntity living) || !(directEntity instanceof LivingEntity attacker)) return amount;
        if (isVampireOrUndead(living))
            return amount * (1 - (hunterLevelCorrection(player) * entry.getLevel() * 1.2F) * 0.1F);
        if (isVampireOrUndead(attacker))
            return amount * (1 - (hunterLevelCorrection(player) * entry.getLevel() * 1.2F) * 0.1F);
        return amount;
    }

}
