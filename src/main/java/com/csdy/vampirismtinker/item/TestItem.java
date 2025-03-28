package com.csdy.vampirismtinker.item;

import de.teamlapen.vampirism.entity.player.hunter.HunterPlayer;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import static com.csdy.vampirismtinker.modifier.method.ModifierUtil.reflectionPenetratingDamage;

public class TestItem extends Item {
    public TestItem() {
        super((new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity target) {
        player.getFoodData().setExhaustion(0);
        player.getFoodData().setFoodLevel(0);
        player.getFoodData().setSaturation(0);
        return super.onLeftClickEntity(stack, player, target);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player) {
            VampirePlayer vampire = VampirePlayer.get(player);
            int vampireLevel = vampire.getLevel();
            if (vampireLevel < 1) {
                reflectionPenetratingDamage(player,player,1000);
                System.out.println(22222222);
                return false; // 返回 false，取消伤害
            }
        }

        // 否则，正常造成伤害
        return super.hurtEnemy(stack, target, attacker);
    }
}
