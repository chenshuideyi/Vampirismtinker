package com.csdy.vampirismtinker.item.material;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class HolyHunterMetal extends Item {
    public HolyHunterMetal() {
        super((new Item.Properties()).stacksTo(64).rarity(Rarity.UNCOMMON));
    }
}
