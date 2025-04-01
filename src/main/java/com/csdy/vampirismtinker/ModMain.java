package com.csdy.vampirismtinker;


import com.csdy.vampirismtinker.effect.EffectsRegister;
import com.csdy.vampirismtinker.fluid.register.FludRegister;
import com.csdy.vampirismtinker.item.ItemRegister;
import com.csdy.vampirismtinker.modifier.method.TestEvent;
import com.csdy.vampirismtinker.modifier.register.ModifierRegister;

import com.csdy.vampirismtinker.particle.register.ParticlesRegister;
import de.teamlapen.vampirism.misc.VampirismCreativeTab;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.CreativeModeTabRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;
import slimeknights.tconstruct.library.client.data.material.MaterialPartTextureGenerator;
import slimeknights.tconstruct.library.client.model.TinkerItemProperties;
import slimeknights.tconstruct.tools.data.sprite.TinkerPartSpriteProvider;

import java.util.Set;

import static com.csdy.vampirismtinker.item.ItemRegister.*;

//TODO: 暂时不知道还有啥
@Mod(ModMain.MODID)
@Mod.EventBusSubscriber(modid = ModMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModMain {

    public static final String MODID = "vampirismtinker";

    public ModMain() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModifierRegister.MODIFIERS.register(bus);
        ItemRegister.ITEMS.register(bus);
        ItemRegister.TINKER_ITEMS.register(bus);
        CsdyTab.CREATIVE_MODE_TABS.register(bus);
        FludRegister.FLUIDS.register(bus);
        EffectsRegister.EFFECT.register(bus);
        ParticlesRegister.PARTICLE_TYPES.register(bus);

        MinecraftForge.EVENT_BUS.register(this);
        bus.addListener(this::addItemsToCreativeTab);


//        EntityRegister.ENTITIES.register(bus);
    }

    private void addItemsToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey().equals(CreativeModeTabs.INGREDIENTS)) {
            event.accept(HUNTER_METAL_REGISTRY_OBJECT.get());
            event.accept(ItemRegister.HOLY_HUNTER_METAL_REGISTRY_OBJECT.get());
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                TinkerItemProperties.registerBrokenProperty(HEART_SEEKER.get());
                TinkerItemProperties.registerToolProperties(HEART_SEEKER.get());
                TinkerItemProperties.registerBrokenProperty(CRUCIFIX.get());
                TinkerItemProperties.registerToolProperties(CRUCIFIX.get());
            });
        }
    }

}

