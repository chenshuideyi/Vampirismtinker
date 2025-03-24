package com.csdy.vampirismtinker.modifier.method;

import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Field;
import java.util.Map;

public class ModifierUtil {

    //默认等级加成
    public static final float hunterPower = 0.2f;
    public static final float vampirePower = 0.2f;
    //DATA_HEALTH_ID
    private static final EntityDataAccessor<Float> DATA_HEALTH_ID = getHealthDataAccessor();

    private static EntityDataAccessor<Float> getHealthDataAccessor() {
        ///因为mc的狗屎混淆所以DATA_HEALTH_ID得写成f_20961_,不然非开发环境会报错,密码个逼
        ///普猫无视这个,因此AOP贯穿是必要的
        try {
//            Field field = LivingEntity.class.getDeclaredField("DATA_HEALTH_ID");
            Field field = LivingEntity.class.getDeclaredField("f_20961_");
            field.setAccessible(true);
            Object value = field.get(null);

            if (value instanceof EntityDataAccessor) {
                return (EntityDataAccessor<Float>) value;
            } else {
                System.err.println("DATA_HEALTH_ID is not of type EntityDataAccessor");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    ///反射贯穿伤害<br/>
    ///target 目标<br/>
    ///player 玩家<br/>
    ///float 造成的伤害<br/>
    /// 不是永远都有用,,,,,
    public static void reflectionPenetratingDamage(Entity target, Player player, float value) {
        if (!(target instanceof LivingEntity living)) return;
        if (DATA_HEALTH_ID == null) return;
        float currentHealth = living.getEntityData().get(DATA_HEALTH_ID);
        float newHealth = currentHealth-value;
        living.getEntityData().set(DATA_HEALTH_ID, newHealth);
        if (living.getHealth() <= 0.0F) {
            living.die(living.level().damageSources().playerAttack(player));
        }
    }

    /**
     * 强制添加效果实例（包括自定义效果）
     * @param entity 目标生物
     * @param instance 效果实例（包含效果类型、时长、等级等）
     */
    public static void forceAddEffect(LivingEntity entity, MobEffectInstance instance) {
        try {
            // 获取效果Map字段（根据mapping调整字段名）
            ///f_20945_
            Field effectsField = LivingEntity.class.getDeclaredField("activeEffects");
            effectsField.setAccessible(true);

            @SuppressWarnings("unchecked")
            Map<MobEffect, MobEffectInstance> effects = (Map<MobEffect, MobEffectInstance>) effectsField.get(entity);
            effects.put(instance.getEffect(), instance);

            if (!entity.level().isClientSide) {
                entity.getAttributes().assignValues(entity.getAttributes()); // 刷新属性
                if (entity instanceof ServerPlayer player) player.connection.send(new ClientboundUpdateMobEffectPacket(entity.getId(), instance));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
//    /**
//     * 生成下落方块(整体移动)
//     *
//     * @param level 服务端
//     * @param pos   区块坐标
//     * @param mx    向量x
//     * @param mz    向量z
//     */
//    public static void spawnFallingBlockByPos(ServerLevel level, BlockPos pos, double mx, double mz) {
//        RandomSource random = RandomSource.create();
//        BlockPos abovePos = new BlockPos(pos).above();//获取上面方块的坐标,以用来判断是否需要生成下落的方块
//        BlockState block = level.getBlockState(pos);//获取下落方块,以用于渲染方块材质
//        BlockState blockAbove = level.getBlockState(abovePos);//获取上面方块的状态,,以用来判断是否需要生成下落的方块
//
//        if (!block.isAir() && block.isRedstoneConductor(level, pos) && !block.hasBlockEntity() && !blockAbove.blocksMotion()) {
//            EntityFallingBlock fallingBlock = new EntityFallingBlock(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, block, 10);
//            fallingBlock.push(mx, 0.2 + random.nextGaussian() * 0.2, mz);
//            level.addFreshEntity(fallingBlock);
//        }
//    }
//
//    /**
//     * 生成下落方块(渲染移动)
//     *
//     * @param level         服务端
//     * @param pos           区块坐标
//     * @param fallingFactor y轴下降系数
//     */
//    public static void spawnFallingBlockByPos(ServerLevel level, BlockPos pos, float fallingFactor) {
//        Random random = new Random();
//        BlockPos abovePos = new BlockPos(pos).above();//获取上面方块的坐标,以用来判断是否需要生成下落的方块
//        BlockState block = level.getBlockState(pos);//获取下落方块,以用于渲染方块材质
//        BlockState blockAbove = level.getBlockState(abovePos);//获取上面方块的状态,,以用来判断是否需要生成下落的方块
//
//        //随机扰动
//        if (random.nextBoolean()) {
//            fallingFactor += (float) (0.4 + random.nextGaussian() * 0.2);
//        } else {
//            fallingFactor -= (float) Mth.clamp(0.2 + random.nextGaussian() * 0.2, 0.2, fallingFactor - 0.1);
//        }
//
//        if (!block.isAir() && block.isRedstoneConductor(level, pos) && !block.hasBlockEntity() && !blockAbove.blocksMotion()) {
//            EntityFallingBlock fallingBlock = new EntityFallingBlock(level, block, (float) (0.32 + fallingFactor * 0.2));
//            fallingBlock.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
//            level.addFreshEntity(fallingBlock);
//        }
//    }
//
//
//    /**
//     * 破坏当前实体坐标为基准周围的方块
//     *
//     * @param level            服务端
//     * @param entity           实体
//     * @param maxBlockHardness 最高破坏硬度
//     * @param destroyRangeX    破坏范围x轴
//     * @param destroyRangeY    破坏范围y轴
//     * @param destroyRangeZ    破坏范围z轴
//     * @param offset           前后偏移量
//     * @param dropBlock        是否掉落方块
//     * @param playSound        是否播放破坏方块的音效
//     * @return 是否破坏成功
//     */
//    public static boolean advancedBreakBlocks(Level level, LivingEntity entity, float maxBlockHardness, int destroyRangeX, int destroyRangeY, int destroyRangeZ, int offsetY, float offset, boolean dropBlock, boolean playSound) {
//        double radians = Math.toRadians(entity.getYRot() + 90);
//        int j1 = Mth.floor(entity.getY());
//        int i2 = Mth.floor(entity.getX() + Math.cos(radians) * offset);
//        int j2 = Mth.floor(entity.getZ() + Math.sin(radians) * offset);
//        boolean flag = false;
//        for (int j = -destroyRangeX; j <= destroyRangeX; ++j) {
//            for (int k2 = offsetY; k2 <= destroyRangeY; ++k2) {
//                for (int k = -destroyRangeZ; k <= destroyRangeZ; ++k) {
//                    int l2 = i2 + j;
//                    int l = j1 + k2;
//                    int i1 = j2 + k;
//                    BlockPos blockpos = new BlockPos(l2, l, i1);
//                    BlockState blockstate = level.getBlockState(blockpos);
//                    if (blockstate.canEntityDestroy(level, blockpos, entity)) {
//                        flag = level.destroyBlock(blockpos, dropBlock, entity) || flag;
//                    }
//                }
//            }
//        }
//        if (flag && playSound) {
//            level.levelEvent(null, 1022, entity.blockPosition(), 0);
//        }
//        return flag;
//    }
}
