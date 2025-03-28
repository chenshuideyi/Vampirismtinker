package com.csdy.vampirismtinker.modifier.vampire;

import com.csdy.vampirismtinker.ModMain;
import com.csdy.vampirismtinker.effect.EffectsRegister;
import com.csdy.vampirismtinker.modifier.VampireBaseModifer;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.List;

import static com.csdy.vampirismtinker.modifier.method.ModifierUtil.forceAddEffect;


public class BloodCharging extends VampireBaseModifer implements MeleeDamageModifierHook, MeleeHitModifierHook,TooltipModifierHook {

    private static final ResourceLocation BLOOD_CHARGING = new ResourceLocation(ModMain.MODID, "blood_charging");

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry entry, ToolAttackContext context, float baseDamage, float damage) {
        float originalDamage = super.getMeleeDamage(tool, entry, context, baseDamage, damage);
        if (originalDamage == 0) return 0;
        Player player = context.getPlayerAttacker();
        if (player == null) return 0;
        ModDataNBT data = tool.getPersistentData();
        float value = data.getFloat(BLOOD_CHARGING);
        return originalDamage + originalDamage* (1 + (0.005f * value * vampireLevelCorrection(player) + 0.1F * entry.getLevel()));
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry entry, ToolAttackContext context, float damageDealt) {
        LivingEntity target = context.getLivingTarget();
        Player player = context.getPlayerAttacker();
        ModDataNBT data = tool.getPersistentData();
        int level = entry.getLevel();
        float value = data.getFloat(BLOOD_CHARGING);
        if (target != null && player != null) {
            VampirePlayer vampire = VampirePlayer.get(player);
            if (value >= 100 +(level -1) * 50){
                vampire.addExhaustion(level);
                player.heal(level);
                return;
            }
            data.putFloat(BLOOD_CHARGING, Math.min(vampireLevelCorrection(player) + value, 100 + (level -1 ) * 50));
        }
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, slimeknights.mantle.client.TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        ModDataNBT persistantData = tool.getPersistentData();
        float value = persistantData.getFloat(BLOOD_CHARGING);
        tooltip.add(Component.translatable("blood_charging.text").withStyle(ChatFormatting.DARK_RED).append(value+"%"));
    }


    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
        hookBuilder.addHook(this, ModifierHooks.TOOLTIP);
        super.registerHooks(hookBuilder);
    }

}
