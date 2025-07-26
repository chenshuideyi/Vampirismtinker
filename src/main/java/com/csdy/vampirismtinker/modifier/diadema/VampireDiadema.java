package com.csdy.vampirismtinker.modifier.diadema;

import com.csdy.tcondiadema.frames.diadema.DiademaType;
import com.csdy.tcondiadema.modifier.DiademaModifier;
import com.csdy.vampirismtinker.diadema.DiademaRegister;
import com.csdy.vampirismtinker.effect.EffectsRegister;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class VampireDiadema extends DiademaModifier {
    @Override
    protected DiademaType getDiademaType() {
        return DiademaRegister.VAMPIRISM.get();
    }

    @Override
    public void onEquip(@NotNull IToolStackView tool, @NotNull ModifierEntry entry, EquipmentChangeContext context) {
        if (context.getLevel() instanceof ServerLevel) {
        if (context.getEntity().hasEffect(EffectsRegister.JUDGMENT.get())) return;
        super.onEquip(tool,entry,context);

        }
    }
}
