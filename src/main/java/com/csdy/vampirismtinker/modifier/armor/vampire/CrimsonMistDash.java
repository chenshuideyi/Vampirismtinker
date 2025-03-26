package com.csdy.vampirismtinker.modifier.armor.vampire;

import com.csdy.vampirismtinker.modifier.VampireBaseModifer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.DamageBlockModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Random;

public class CrimsonMistDash extends VampireBaseModifer implements DamageBlockModifierHook {

    private static final Random random = new Random();
    private static final double DODGE_CHANCE = 66/2.8; // 14级66闪避


    @Override
    public boolean isDamageBlocked(IToolStackView tool, ModifierEntry entry, EquipmentContext context, EquipmentSlot slot, DamageSource damageSource, float amount) {
        if (damageSource.getEntity() == null && damageSource.getDirectEntity() == null) return false;
        if (!(context.getEntity() instanceof Player player)) return false;
        if (isTakingSundamage(player)) return false;
        if (random.nextInt(100) + 1 < DODGE_CHANCE * vampireLevelCorrection(player)) {
            mistDash(player,damageSource.getEntity());
            return true;
        }

        return false;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.DAMAGE_BLOCK);
        super.registerHooks(hookBuilder);
    }


    private void mistDash(Player player, Entity attacker) {
        if (player.level().isClientSide) return;

        Vec3 toPlayer = player.position().subtract(attacker.position());
        Vec3 initialDirection = new Vec3(toPlayer.x, 0, toPlayer.z).normalize(); // 忽略Y轴

        // -90° 到 90）
        RandomSource random = player.getRandom();
        double angle = (random.nextDouble() * 180 - 90) * Math.PI / 180; // [-90°, 90°] 转弧度

        // 2D XZ平面旋转
        Vec3 dashDirection = new Vec3(
                initialDirection.x * Math.cos(angle) - initialDirection.z * Math.sin(angle),
                0,
                initialDirection.x * Math.sin(angle) + initialDirection.z * Math.cos(angle)
        ).normalize();

        //速度
        double dashSpeed = 1.6;
        player.setDeltaMovement(
                dashDirection.x * dashSpeed,
                player.getDeltaMovement().y,
                dashDirection.z * dashSpeed
        );
        player.hurtMarked = true; // 强制同步运动数据,很重要

        spawnMistParticles(player, 100);
    }

    private void spawnMistParticles(Player player, int count) {
        //令神龙制造烟雾
        if (player.level() instanceof ServerLevel serverLevel) {
            Vec3 pos = player.position();
            serverLevel.sendParticles(
                            ParticleTypes.CLOUD,
                            pos.x, pos.y + 1.0, pos.z,
                            count,
                            0.7, 0.7, 0.7,
                            0.05
            );
        }
    }
    @Override
    public boolean isNoLevels() {
        return true;
    }


    public static boolean transformToBat(Player player) {


        return false;
    }
}
