package com.csdy.vampirismtinker.diadema.vampirism;

import com.csdy.tcondiadema.frames.diadema.ClientDiadema;

import com.csdy.tcondiadema.particleUtils.PointSets;
import com.csdy.vampirismtinker.particle.register.ParticlesRegister;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class VampirismClientDiadema extends ClientDiadema {
    Random random = new Random();
    private static final double RADIUS = 16;
    private static final SimpleParticleType PARTICLE_TYPE = ParticlesRegister.CRIMSON_MIST_PARTICLE.get();

    // 控制粒子发射频率（每3秒一次）
    private static final int PARTICLE_INTERVAL = 10; // 60 ticks = 3秒
    private int timer = 0;

    @Override
    protected void perTick() {
        var level = Minecraft.getInstance().level;
        var player = Minecraft.getInstance().player;

        if (level == null || player == null) return;
        if (!level.dimension().location().equals(getDimension())) return;

        // 计时器控制
        if (++timer >= PARTICLE_INTERVAL) {
            timer = 0;
            burstParticles(level);
        }
    }

    private void burstParticles(Level level) {

        int count = 666;

        // 不规则分布
        for (int i = 0; i < count; i++) {
            // 随机角度和距离
            double angle = random.nextDouble() * Math.PI * 2;
            double distance = RADIUS / 2 * (0.5 + random.nextDouble() * 0.5);

            // 计算位置
            double xOffset = Math.cos(angle) * distance;
            double zOffset = Math.sin(angle) * distance;
            double yOffset = random.nextDouble() * 2;

            // 添加随机速度
            double xSpeed = (random.nextDouble() - 0.5) * 0.1;
            double ySpeed = random.nextDouble() * 0.05;
            double zSpeed = (random.nextDouble() - 0.5) * 0.1;

            // 生成粒子
            level.addParticle(PARTICLE_TYPE,
                    getPosition().x + xOffset,
                    getPosition().y + yOffset,
                    getPosition().z + zOffset,
                    xSpeed, ySpeed, zSpeed);
        }
    }
}
