package com.csdy.vampirismtinker.modifier.register;

import com.csdy.vampirismtinker.modifier.InscribedPrayers;
import com.csdy.vampirismtinker.modifier.tool.CatholicVampireHunt;
import com.csdy.vampirismtinker.modifier.BloodCharging;
import com.csdy.vampirismtinker.modifier.tool.Crucio;
import com.csdy.vampirismtinker.modifier.Test;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

import static com.csdy.vampirismtinker.ModMain.MODID;

public class ModifierRegister {
    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(MODID);
    public static final StaticModifier<Test> TEST_STATIC_MODIFIER = MODIFIERS.register("test", Test::new);
    public static final StaticModifier<CatholicVampireHunt> CATHOLIC_VAMPIRE_HUNT_STATIC_MODIFIER = MODIFIERS.register("catholic_vampire_hunt", CatholicVampireHunt::new);
    public static final StaticModifier<BloodCharging> BLOOD_CHARGING_STATIC_MODIFIER = MODIFIERS.register("blood_charging", BloodCharging::new);
    public static final StaticModifier<Crucio> CRUCIO_STATIC_MODIFIER = MODIFIERS.register("crucio", Crucio::new);
    public static final StaticModifier<InscribedPrayers> INSCRIBED_PRAYERS_STATIC_MODIFIER = MODIFIERS.register("inscribed_prayers", InscribedPrayers::new);

















}
