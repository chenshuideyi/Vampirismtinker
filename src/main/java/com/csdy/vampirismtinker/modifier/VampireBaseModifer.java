package com.csdy.vampirismtinker.modifier;

import com.csdy.vampirismtinker.effect.EffectsRegister;
import de.teamlapen.vampirism.entity.player.hunter.HunterPlayer;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import static com.csdy.vampirismtinker.modifier.method.ModifierUtil.forceAddEffect;

public class VampireBaseModifer extends Modifier implements MeleeDamageModifierHook, InventoryTickModifierHook, EquipmentChangeModifierHook {
    public static final float vampirePower = 0.2f;
    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry entry, ToolAttackContext context, float baseDamage, float damage) {
        Player player = context.getPlayerAttacker();
        if (player == null) return 0;
        boolean message = !player.getCommandSenderWorld().isClientSide;
        VampirePlayer vampire = VampirePlayer.get(player);
        if (vampire.getLevel() < 1) {
            if (message) player.displayClientMessage(Component.translatable("text.vampirism.can_not_be_used_faction"), true);
            return 0;
        }
        return damage;
    }

    protected int vampirePlayerLevel(Player player) {
        if (player == null || !player.isAlive()) {
            return 0; // 默认值
        }
        VampirePlayer vampire = VampirePlayer.get(player);
        return vampire != null ? vampire.getLevel() : 0;
    }

    protected float vampireLevelCorrection(Player player){
        return vampirePlayerLevel(player) * vampirePower;
    }

    protected int hunterPlayerLevel(Player player){
        HunterPlayer hunter = HunterPlayer.get(player);
        return hunter.getLevel();
    }

    public boolean isNoLevels() {
        return false;
    }

    @Override
    public void onEquip(@NotNull IToolStackView tool, @NotNull ModifierEntry entry, EquipmentChangeContext context) {
        if (!(context.getLevel() instanceof ServerLevel)) return;
        LivingEntity entity = context.getEntity();
        if (!(entity instanceof Player player)) return;
        if (context.getChangedSlot().isArmor()) {
            if (vampirePlayerLevel(player) < 1 && player.isAlive()) {
                boolean message = !player.getCommandSenderWorld().isClientSide;
                if (!message) return;
                player.displayClientMessage(Component.translatable("text.vampirismtinker.blood_curse_erosion"), true);
            }
        }
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (!(holder instanceof Player player)) return;
        if (isCorrectSlot && vampirePlayerLevel(player) < 1 && player.isAlive()) {
            forceAddEffect(holder, (new MobEffectInstance(EffectsRegister.BLOOD_CURSE_EROSION.get(), 120, 0)));
        }
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE);
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK);
        hookBuilder.addHook(this, ModifierHooks.EQUIPMENT_CHANGE);
    }

    @Override
    public @NotNull Component getDisplayName(int level) {
        if (isNoLevels()) {
            return super.getDisplayName();
        } else {
            return super.getDisplayName(level);
        }
    }

}
