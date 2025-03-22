package com.csdy.item.tool;

import com.csdy.item.tool.until.ToolDefinitions;
import de.teamlapen.vampirism.util.DamageHandler;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.tools.item.ModifiableSwordItem;


public class TicCrucifix extends ModifiableSwordItem {
    public TicCrucifix(Properties properties) {
        super(properties, ToolDefinitions.CRUCIFIX);
    }


}
