package com.csdy.modifier.method;

import com.csdy.entity.EntityFallingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class ModifierUntil {
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
