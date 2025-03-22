package com.csdy.item.tool;

import com.csdy.item.tool.until.ToolDefinitions;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.util.DamageHandler;
import de.teamlapen.vampirism.util.PlayerAttackDamageSourceBypassArmor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.item.ModifiableSwordItem;


public class TicHeartseeker extends ModifiableSwordItem {
    public TicHeartseeker(Properties properties) {
        super(properties, ToolDefinitions.HEART_SEEKER);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (!(entity instanceof LivingEntity target)) return super.onLeftClickEntity(stack, player, entity);
        float baseDamage = ToolAttackUtil.getAttributeAttackDamage(ToolStack.from(stack), player, EquipmentSlot.MAINHAND);
        target.invulnerableTime = 0;
        DamageHandler.hurtModded(target, s -> s.getPlayerAttackWithBypassArmor(player), baseDamage*0.8f);
        return super.onLeftClickEntity(stack, player, entity);
    }




}
