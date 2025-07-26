package com.csdy.vampirismtinker.diadema;

import com.csdy.tcondiadema.frames.CsdyRegistries;
import com.csdy.tcondiadema.frames.diadema.ClientDiademaType;
import com.csdy.vampirismtinker.ModMain;
import com.csdy.vampirismtinker.diadema.vampirism.VampirismClientDiadema;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@OnlyIn(Dist.CLIENT)
public class ClientDiademaRegister {
    public static final DeferredRegister<ClientDiademaType> CLIENT_DIADEMA_TYPES = DeferredRegister.create(CsdyRegistries.CLIENT_DIADEMA_TYPE, ModMain.MODID);

    public static final RegistryObject<ClientDiademaType> VAMPIRISM =
            CLIENT_DIADEMA_TYPES.register("vampirism", () -> ClientDiademaType.Create(VampirismClientDiadema::new));



}
