package com.csdy.vampirismtinker;

import com.csdy.vampirismtinker.smeltery.VampirismTinkerSmelteryRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.fluids.data.FluidBucketModelProvider;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = ModMain.MODID,bus=Mod.EventBusSubscriber.Bus.MOD)
public class GatherData {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), new VampirismTinkerSmelteryRecipeProvider(output));
        generator.addProvider(event.includeClient(), new FluidBucketModelProvider(output, ModMain.MODID));
    }
}
