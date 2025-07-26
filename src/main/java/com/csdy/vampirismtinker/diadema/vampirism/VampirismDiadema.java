package com.csdy.vampirismtinker.diadema.vampirism;

import com.csdy.tcondiadema.diadema.api.ranges.SphereDiademaRange;
import com.csdy.tcondiadema.frames.diadema.Diadema;
import com.csdy.tcondiadema.frames.diadema.DiademaType;
import com.csdy.tcondiadema.frames.diadema.movement.DiademaMovement;
import com.csdy.tcondiadema.frames.diadema.range.DiademaRange;
import com.csdy.vampirismtinker.effect.EffectsRegister;
import de.teamlapen.vampirism.core.ModBlocks;
import de.teamlapen.vampirism.core.ModEffects;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import lombok.NonNull;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class VampirismDiadema extends Diadema {
    final static double RADIUS = 16;
    private final Entity holder = getCoreEntity();
    private final SphereDiademaRange range = new SphereDiademaRange(this, RADIUS);

    public VampirismDiadema(DiademaType type, DiademaMovement movement) {
        super(type, movement);
    }

    @Override
    public @NonNull DiademaRange getRange() {
        return range;
    }

    @Override
    protected void perTick() {

        if (!(holder instanceof LivingEntity livingEntity)) return;

        if (livingEntity.hasEffect(EffectsRegister.JUDGMENT.get())) this.kill();

        for (Entity entity : affectingEntities) {
            if (!(entity instanceof LivingEntity living)) continue;
            living.addEffect(new MobEffectInstance(ModEffects.SUNSCREEN.get(),100,10));

        }

        var blocks = range.getAffectingBlocks();
        //java比较弱智，Stream不能用来for,只能foreach
        blocks.forEach(blockPos -> {
            BlockState currentState = getLevel().getBlockState(blockPos);

            // 跳过空气和已经是诅咒土的情况
            if (currentState.isAir() || currentState.is(ModBlocks.CURSED_EARTH.get())) {
                return;
            }

            // 使用标签检查是否是泥土类方块
            if (currentState.is(BlockTags.DIRT) || currentState.is(BlockTags.REPLACEABLE_BY_TREES)) {
                getLevel().setBlockAndUpdate(blockPos, ModBlocks.CURSED_EARTH.get().defaultBlockState());
            }
        });

        if (holder instanceof Player player) {
            VampirePlayer vampire = VampirePlayer.get(player);
            vampire.drinkBlood(1,1,null);
        }
    }


}
