package com.csdy.vampirismtinker;


import com.csdy.vampirismtinker.item.ItemRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.tconstruct.library.tools.helper.ToolBuildHandler;
import slimeknights.tconstruct.library.tools.item.IModifiable;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.csdy.vampirismtinker.ModMain.MODID;



public class CsdyTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    private static void acceptTool(Consumer<ItemStack> output, Supplier<? extends IModifiable> tool) {
        ToolBuildHandler.addVariants(output, (IModifiable)tool.get(), "");
    }

    private static void addToolItems(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output tab) {
        Objects.requireNonNull(tab);
        Consumer<ItemStack> output = tab::accept;
        acceptTool(output,ItemRegister.HEART_SEEKER);
        acceptTool(output,ItemRegister.CRUCIFIX);
    }

    public static final RegistryObject<CreativeModeTab> TOOL_TAB = CREATIVE_MODE_TABS.register("tiac_tool", () -> CreativeModeTab.builder()
            .title(Component.translatable("vampirismtinker_tool_tab"))
            .icon(() -> new ItemStack(ItemRegister.CRUCIFIX.get()))
            .displayItems(CsdyTab::addToolItems).build());
}


