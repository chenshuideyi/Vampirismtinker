package com.csdy.vampirismtinker.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

import static com.csdy.vampirismtinker.modifier.method.ModifierUtil.reflectionPenetratingDamage;

public class Test extends Item {
    public Test() {
        super(new Properties().stacksTo(1).fireResistant().rarity(Rarity.COMMON));
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        reflectionPenetratingDamage(entity, (Player) entity,100);
        return super.onEntitySwing(stack, entity);
    }

    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        InteractionResultHolder<ItemStack> use = super.use(world, player, hand);//能使用物品的一定是玩家 所以改成player
        reflectionPenetratingDamage(player,player,100);
        return use;
    }
}
