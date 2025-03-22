package com.csdy.modifier;

import com.csdy.ModMain;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public class BloodCharging extends Modifier implements MeleeDamageModifierHook, MeleeHitModifierHook,TooltipModifierHook {
    private static final float vampirePower = 0.2f;
    private static final ResourceLocation BLOODCHARGING = new ResourceLocation(ModMain.MODID, "blood_charging");
    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifierEntry, ToolAttackContext context, float baseDamage, float damage) {
        Player player = context.getPlayerAttacker();
        if (player == null) return 0;
        boolean message = !player.getCommandSenderWorld().isClientSide;
        VampirePlayer vampire = VampirePlayer.get(player);
        if (vampire.getLevel() < 1) {
            if (message) player.displayClientMessage(Component.translatable("text.vampirism.can_not_be_used_faction"), true);
            return 0;
        }
        ModDataNBT persistantData = tool.getPersistentData();
        float value = persistantData.getFloat(BLOODCHARGING);
        return damage * (1 + 0.01f * value * vampire.getLevel()*vampirePower);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity target = context.getLivingTarget();
        Player player = context.getPlayerAttacker();
        ModDataNBT data = tool.getPersistentData();
        float value = data.getFloat(BLOODCHARGING);
        if (value >= 100+(modifier.getLevel()-1)*50) return;
        if (target != null && player != null) {
            VampirePlayer vampire = VampirePlayer.get(player);
            data.putFloat(BLOODCHARGING, Math.min(vampire.getLevel()*vampirePower+value,100+(modifier.getLevel()-1)*50));
        }
    }


///为什么攻速不走这个接口????????
//    @Override
//    public float modifyStat(IToolStackView tool, ModifierEntry modifier, LivingEntity holder, FloatToolStat stat, float baseValue, float multiplier) {
//        if (!(holder instanceof Player player)) return baseValue;
//        ModDataNBT persistantData = tool.getPersistentData();
//        float value = persistantData.getFloat(BLOODCHARGING);
//        VampirePlayer vampire = VampirePlayer.get(player);
//        if (persistantData.contains(BLOODCHARGING, 5)) {
//            if (stat == ToolStats.ATTACK_DAMAGE) {
//                return baseValue * (1 + 0.01f * value * vampire.getLevel()*0.2f);
//            }
//            if (stat == ToolStats.ATTACK_SPEED) {
//                return baseValue * (1 + 0.01f * value * vampire.getLevel()*0.2f);
//            }
//
//        }return baseValue;
//    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, slimeknights.mantle.client.TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        ModDataNBT persistantData = tool.getPersistentData();
        float value = persistantData.getFloat(BLOODCHARGING);
        tooltip.add(Component.translatable("blood_charging.text").withStyle(ChatFormatting.DARK_RED).append(value+"%"));
    }


    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE);
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
        hookBuilder.addHook(this, ModifierHooks.TOOLTIP);
    }

}
