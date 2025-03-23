package com.csdy.vampirismtinker.smeltery;

import com.csdy.vampirismtinker.fluid.register.FludRegister;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.smeltery.data.SmelteryRecipeProvider;

import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipeBuilder;

import java.util.function.Consumer;

public class VampirismTinkerSmelteryRecipeProvider extends SmelteryRecipeProvider {
    public VampirismTinkerSmelteryRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    private void addAlloyRecipes(Consumer<FinishedRecipe> consumer) {
        String folder = "smeltery/alloys/";


    // hunter metal: 5 copper + 1 gold = 1
        AlloyRecipeBuilder.alloy(FludRegister.HUNTER_METAL, FluidValues.INGOT)
                .addInput(TinkerFluids.moltenCopper.ingredient(FluidValues.INGOT*5))
                .addInput(TinkerFluids.moltenIron.ingredient(FluidValues.INGOT))
                .save(consumer, prefix(FludRegister.HUNTER_METAL, folder));
    }
}
