package com.csdy.vampirismtinker.modifier.armor.vampire;

import com.csdy.vampirismtinker.ModMain;
import com.csdy.vampirismtinker.modifier.VampireBaseModifer;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.List;

public class MidnightAristocrat extends VampireBaseModifer implements OnAttackedModifierHook, TooltipModifierHook {

    private static final ResourceLocation VELVET_SHADOW = new ResourceLocation(ModMain.MODID, "velvet_shadow");


    @Override
    public void onAttacked(IToolStackView tool, ModifierEntry entry, EquipmentContext context, EquipmentSlot slot, DamageSource damageSource, float amount, boolean isDirectDamage) {
        if (!(context.getEntity() instanceof Player player)) return;
        if ((!(player.level() instanceof ServerLevel serverLevel)) || serverLevel.getDayTime()> 13000) return;
        ModDataNBT data = tool.getPersistentData();
        float value = data.getFloat(VELVET_SHADOW);
        data.putFloat(VELVET_SHADOW, Math.max(value - amount,0));
        player.heal(amount/2);
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry entry, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        int level = entry.getLevel();
        if (holder instanceof Player player && isCorrectSlot) {
            if ((world instanceof ServerLevel serverLevel) && serverLevel.getDayTime()> 13000) {
                ModDataNBT data = tool.getPersistentData();
                float value = data.getFloat(VELVET_SHADOW);
                if (value >= level * 50) return;
                data.putFloat(VELVET_SHADOW, Math.min(vampireLevelCorrection(player) + value, level * 50));
            }
        }

        super.onInventoryTick(tool, entry, world, holder, itemSlot, isSelected, isCorrectSlot, stack);
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, slimeknights.mantle.client.TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        ModDataNBT persistantData = tool.getPersistentData();
        float value = persistantData.getFloat(VELVET_SHADOW);
        tooltip.add(Component.translatable("velvet_shadow.text").withStyle(ChatFormatting.GRAY).append(value+"%"));
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.ON_ATTACKED);
        hookBuilder.addHook(this, ModifierHooks.TOOLTIP);
        super.registerHooks(hookBuilder);
    }
}
