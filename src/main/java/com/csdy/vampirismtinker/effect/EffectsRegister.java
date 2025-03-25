package com.csdy.vampirismtinker.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.csdy.vampirismtinker.ModMain.MODID;

public class EffectsRegister {
    public static final DeferredRegister<MobEffect> EFFECT = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS,MODID);

    public static final RegistryObject<MobEffect> SILVER_FLAME_BRAND = EFFECT.register("silver_flame_brand", SilverflameBrand::new);
    public static final RegistryObject<MobEffect> BLOOD_CURSE_EROSION = EFFECT.register("blood_curse_erosion", BloodcurseErosion::new);
    public static final RegistryObject<MobEffect> SCARLET_FRENZY = EFFECT.register("scarlet_frenzy", ScarletFrenzy::new);
}
