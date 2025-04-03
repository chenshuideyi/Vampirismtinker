package com.csdy.vampirismtinker.modifier.method;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;

public class Test {
//    public LivingEntity entity;
//    public void actuallyHurt(DamageSource source, float amount, boolean special) {
//        try {
//
//            if (entity.level().isClientSide) {
//                return;
//            } else if (entity.isDeadOrDying() && !EntityDataInjector.getAllowHurtMethod(entity)) {
//                return;
//            }
//            net.minecraftforge.common.ForgeHooks.onLivingAttack(entity, source, amount);
//            CatchActuallyHurt0Event.PrePre event = new CatchActuallyHurt0Event.PrePre(entity, source, amount);
//            amount = event.getAmount();
//            if (MinecraftForge.EVENT_BUS.post(event)) return;
//            if (entity.isSleeping() && !entity.level().isClientSide) {
//                entity.stopSleeping();
//            }
//            entity.noActionTime = (0);
//            float f = amount;
//            boolean flag = false;
//            if (amount > 0.0F && entity.isDamageSourceBlocked(source)) {
//                net.minecraftforge.event.entity.living.ShieldBlockEvent ev = net.minecraftforge.common.ForgeHooks.onShieldBlock(entity, source, amount);
//                if (!ev.isCanceled()) {
//                    if (ev.shieldTakesDamage()) entity.hurtCurrentlyUsedShield(amount);
//                }
//            }
//            if (source.is(DamageTypeTags.IS_FREEZING) && entity.getType().is(EntityTypeTags.FREEZE_HURTS_EXTRA_TYPES)) {
//                amount *= 5.0F;
//            }
//
//            entity.walkAnimation.setSpeed(1.5F);
//            entity.lastHurt = (amount);
//            entity.invulnerableTime = 20;
//            EntityExpandedContent entityEC = ((LivingEntityEC) entity).uom$livingECData();
//            if (entityEC.invulnerableTime > 10) {
//                float lastFeHurt = entityEC.lastFEHurt;
//                if (amount < lastFeHurt)
//                    return;
//                actuallyHurt0(source, amount - lastFeHurt, special);
//            } else {
//                entityEC.lastFEHurt = amount;
//                actuallyHurt0(source, amount, special);
//            }
//            entity.hurtTime = entity.hurtDuration;
//            if (source.is(DamageTypeTags.DAMAGES_HELMET) && !entity.getItemBySlot(EquipmentSlot.HEAD).isEmpty()) {
//                entity.hurtHelmet(source, amount);
//                amount *= 0.75F;
//            }
//
//            Entity entity1 = source.getEntity();
//            if (entity1 != null) {
//                if (entity1 instanceof LivingEntity livingentity1) {
//                    if (!source.is(DamageTypeTags.NO_ANGER)) {
//                        entity.setLastHurtByMob(livingentity1);
//                    }
//                }
//
//                if (entity1 instanceof Player player1) {
//                    entity.lastHurtByPlayerTime = (100);
//                    entity.lastHurtByPlayer = (player1);
//                } else if (entity1 instanceof net.minecraft.world.entity.TamableAnimal tamableEntity) {
//                    if (tamableEntity.isTame()) {
//                        entity.lastHurtByPlayerTime = (100);
//                        LivingEntity livingentity2 = tamableEntity.getOwner();
//                        if (livingentity2 instanceof Player player) {
//                            entity.lastHurtByPlayer = (player);
//                        } else {
//                            entity.lastHurtByPlayer = null;
//                        }
//                    }
//                }
//            }
//            if (!source.is(DamageTypeTags.NO_IMPACT)) {
//                markHurt(entity);
//            }
//
//            if (entity1 != null && !source.is(DamageTypeTags.IS_EXPLOSION)) {
//                double d0 = entity1.getX() - entity.getX();
//
//                double d1;
//                for (d1 = entity1.getZ() - entity.getZ(); d0 * d0 + d1 * d1 < 1.0E-4D; d1 = (Math.random() - Math.random()) * 0.01D) {
//                    d0 = (Math.random() - Math.random()) * 0.01D;
//                }
//
//                entity.knockback(0.4F, d0, d1);
//                if (!flag) {
//                    entity.indicateDamage(d0, d1);
//                }
//            }
//            entity.level().broadcastDamageEvent(entity, source);
//            if (entity.isDeadOrDying()) {
//                if (!checkTotemDeathProtection(entity, source)) {
//                    SoundEvent soundevent = getDeathSound(entity);
//                    if (soundevent != null) {
//                        if (special) {
//                            entity.playSound(soundevent, getSoundVolume(entity), entity.getVoicePitch());
//                        }
//                    }
//
//                    die(entity, source);
//                }
//            } else {
//                if (special)
//                    playHurtSound(entity, source);
//            }
//            entity.lastDamageSource = (source);
//            entity.lastDamageStamp = (entity.level().getGameTime());
//            if (entity instanceof ServerPlayer) {
//                CriteriaTriggers.ENTITY_HURT_PLAYER.trigger((ServerPlayer) entity, source, f, amount, flag);
//            }
//            if (entity1 instanceof ServerPlayer) {
//                CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((ServerPlayer) entity1, entity, source, f, amount, flag);
//            }
//        } catch (Throwable throwable) {
//            throwable.printStackTrace();
//        }
//    }
}
