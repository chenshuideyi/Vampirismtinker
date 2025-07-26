package com.csdy.vampirismtinker.diadema;

import com.csdy.tcondiadema.frames.CsdyRegistries;
import com.csdy.tcondiadema.frames.diadema.DiademaType;
import com.csdy.vampirismtinker.ModMain;
import com.csdy.vampirismtinker.diadema.vampirism.VampirismDiadema;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class DiademaRegister {
    public static final DeferredRegister<DiademaType> DIADEMA_TYPES = DeferredRegister.create(CsdyRegistries.DIADEMA_TYPE, ModMain.MODID);

    public static final RegistryObject<DiademaType> VAMPIRISM =
            DIADEMA_TYPES.register("vampirism", () -> DiademaType.create(VampirismDiadema::new));

}
