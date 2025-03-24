package com.csdy.vampirismtinker.modifier.register;

import com.csdy.vampirismtinker.modifier.armor.hunter.HolyRomanRebornWarrior;
import com.csdy.vampirismtinker.modifier.armor.hunter.InscribedPrayersArmor;
import com.csdy.vampirismtinker.modifier.hunter.AngelDust;
import com.csdy.vampirismtinker.modifier.hunter.InscribedPrayers;
import com.csdy.vampirismtinker.modifier.tool.CatholicVampireHunt;
import com.csdy.vampirismtinker.modifier.vampire.BloodCharging;
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

    //猎人相关
    public static final StaticModifier<InscribedPrayers> INSCRIBED_PRAYERS_STATIC_MODIFIER = MODIFIERS.register("inscribed_prayers", InscribedPrayers::new);
    public static final StaticModifier<AngelDust> ANGEL_DUST_STATIC_MODIFIER = MODIFIERS.register("angel_dust", AngelDust::new);
    public static final StaticModifier<InscribedPrayersArmor> INSCRIBED_PRAYERS_ARMOR_STATIC_MODIFIER = MODIFIERS.register("inscribed_prayers_armor", InscribedPrayersArmor::new);
    public static final StaticModifier<HolyRomanRebornWarrior> HOLY_ROMAN_REBORN_WARRIOR_STATIC_MODIFIER = MODIFIERS.register("holy_roman_reborn_warrior", HolyRomanRebornWarrior::new);















}
