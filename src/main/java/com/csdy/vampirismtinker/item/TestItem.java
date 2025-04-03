package com.csdy.vampirismtinker.item;

import com.csdy.vampirismtinker.modifier.method.ModifierUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import static com.csdy.vampirismtinker.modifier.method.HealthReflector.unsafeSetHealth;


public class TestItem extends Item {
    public TestItem(){
        super((new Item.Properties()).stacksTo(1).rarity(Rarity.UNCOMMON));
    }
    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity target) {
        if (target instanceof LivingEntity living) {
            dropLoot(living,living.level().damageSources().playerAttack(player));
            setEntityDead(living);
//            living.setPose(Pose.DYING);
//            living.setHealth(0);
//                ModifierUtil.reflectionSetHealth(living,player,0);
        }
        return true; // 取消原版伤害
    }

    private static void dropLoot(LivingEntity entity, DamageSource ds) {
        try {
            Method dropAllDeathLootMethod = ObfuscationReflectionHelper.findMethod(LivingEntity.class, "dropAllDeathLoot", DamageSource.class);
//            Method dropAllDeathLootMethod = ObfuscationReflectionHelper.findMethod(LivingEntity.class, "m_6668_", DamageSource.class);
            dropAllDeathLootMethod.invoke(entity, ds);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void setEntityDead(LivingEntity entity) {
        try {
            Field deadField = ObfuscationReflectionHelper.findField(LivingEntity.class, "dead");
//            Field deadField = ObfuscationReflectionHelper.findField(LivingEntity.class, "f_20890_"); // dead
            deadField.setBoolean(entity, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


