package com.csdy;


import com.csdy.item.ItemRegister;
import com.csdy.item.tool.until.TicItemRegister;
import com.csdy.modifier.register.ModifierRegister;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Collections;
import java.util.Set;


@Mod(ModMain.MODID)
@Mod.EventBusSubscriber(modid = ModMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModMain {

    public static final String MODID = "vampirismtinker";

    public ModMain() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModifierRegister.MODIFIERS.register(bus);
        ItemRegister.ITEMS.register(bus);
        ItemRegister.TINKER_ITEMS.register(bus);

    }

}

