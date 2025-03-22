package com.csdy.modifier.register;

import com.csdy.modifier.CatholicVampireHunt;
import com.csdy.modifier.BloodCharging;
import com.csdy.modifier.Test;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

import static com.csdy.ModMain.MODID;

public class ModifierRegister {
    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(MODID);
    public static final StaticModifier<Test> TEST_STATIC_MODIFIER = MODIFIERS.register("test", Test::new);
    public static final StaticModifier<CatholicVampireHunt> CATHOLIC_VAMPIRE_HUNT_STATIC_MODIFIER = MODIFIERS.register("catholic_vampire_hunt", CatholicVampireHunt::new);
    public static final StaticModifier<BloodCharging> CRUCIO_STATIC_MODIFIER = MODIFIERS.register("blood_charging", BloodCharging::new);




















}
