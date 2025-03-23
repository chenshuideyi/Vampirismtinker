package com.csdy.vampirismtinker.item.material;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class HunterMetal extends Item {
    public HunterMetal() {
        super((new Item.Properties()).stacksTo(64).rarity(Rarity.COMMON));
    }
}
