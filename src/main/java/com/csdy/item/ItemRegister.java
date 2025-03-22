package com.csdy.item;

import com.csdy.ModMain;
import com.csdy.item.tool.TicHeartseeker;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.common.registration.ItemDeferredRegisterExtension;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;

import static com.csdy.ModMain.MODID;

public class ItemRegister {
    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final ItemDeferredRegisterExtension TINKER_ITEMS = new ItemDeferredRegisterExtension(MODID);
    public static final RegistryObject<Item> TEST_ITEM = ITEMS.register("test_item", TestItem::new);
    public static final ItemObject<ModifiableItem> HEART_SEEKER = TINKER_ITEMS.register("tic_heart_seeker",()->new TicHeartseeker(new Item.Properties().stacksTo(1)));
}
