package com.csdy.vampirismtinker.modifier.method;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.csdy.vampirismtinker.ModMain.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TestEvent {
//    @SubscribeEvent(priority = EventPriority.HIGHEST)
//    public static void LivingDeathVampire(LivingDeathEvent event) {
//        //吸血鬼护甲免死相关
//        if (event.getEntity() instanceof Player player) {
//            Minecraft mc = Minecraft.getInstance();
//            player.setHealth(player.getMaxHealth());
//            player.deathTime = -2;
//            player.isAlive();
//            event.setCanceled(true);
//        }
//    }
}
