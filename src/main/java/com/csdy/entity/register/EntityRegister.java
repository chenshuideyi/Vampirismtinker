package com.csdy.entity.register;

import com.csdy.ModMain;
import com.csdy.entity.EntityFallingBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityRegister {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ModMain.MODID);

    //下落的方块
    public static final RegistryObject<EntityType<EntityFallingBlock>> FALLING_BLOCK =
            ENTITIES.register("falling_block",
                    () -> EntityType.Builder.<EntityFallingBlock>of(EntityFallingBlock::new, MobCategory.MISC)
                            .sized(0.99f, 0.99f)
                            .build(new ResourceLocation(ModMain.MODID, "falling_block").toString()));



}
