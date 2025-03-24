package com.csdy.vampirismtinker.fluid.register;

import com.csdy.vampirismtinker.ModMain;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.entity.vampire.VampireBaseEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import slimeknights.mantle.registration.deferred.FluidDeferredRegister;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.fluids.block.BurningLiquidBlock;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import static slimeknights.tconstruct.fluids.block.BurningLiquidBlock.createBurning;

public class FludRegister {
    protected static boolean isVampireOrUndead(LivingEntity entity) {
        if (entity instanceof VampireBaseEntity) {
            return true;
        }
        if (entity instanceof Player player){
            VampirePlayer vampire = VampirePlayer.get(player);
            if (vampire.getLevel() > 0) return true;
        }
        return entity.getMobType() == MobType.UNDEAD;
    }

    public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(ModMain.MODID);
    protected static Map<FluidObject<ForgeFlowingFluid>,Boolean> FLUID_MAP = new HashMap<>();
    public static Set<FluidObject<ForgeFlowingFluid>> getFluids(){
        return FLUID_MAP.keySet();
    }
    public static Map<FluidObject<ForgeFlowingFluid>,Boolean> getFluidMap(){
        return FLUID_MAP;
    }
    /// register FLUIDS<br/>
    /// name 名字<br/>
    /// temp 温度<br/>
    /// lightLevel 光照等级<br/>
    /// burnTime 燃烧时间<br/>
    /// damage 接触伤害<br/>
    /// gas 如果是ture则变成倒过来的桶
    private static FluidObject<ForgeFlowingFluid> registerHotBurning(FluidDeferredRegister register,String name,int temp,int lightLevel,int burnTime,float damage,boolean gas){
        FluidObject<ForgeFlowingFluid> object = register.register(name).type(hot(name,temp,gas)).bucket().block(createBurning(MapColor.COLOR_GRAY,lightLevel,burnTime,damage)).commonTag().flowing();
        FLUID_MAP.put(object,gas);
        return object;
    }
    private static FluidObject<ForgeFlowingFluid> registerFluid(FluidDeferredRegister register, String name, int temp, Function<Supplier<? extends FlowingFluid>, LiquidBlock> blockFunction, boolean gas){
        FluidObject<ForgeFlowingFluid> object = register.register(name).type(hot(name,temp,gas)).bucket().block(blockFunction).commonTag().flowing();
        FLUID_MAP.put(object,gas);
        return object;
    }

    public static final FluidObject<ForgeFlowingFluid> HUNTER_METAL = registerHotBurning(FLUIDS,"hunter_metal",777,1,4,1f,false);

    public static final FluidObject<ForgeFlowingFluid> HOLY_HUNTER_METAL = registerHotBurning(FLUIDS,"holy_hunter_metal",1777,15,8,2f,false);

    public static final FluidObject<ForgeFlowingFluid> HOLY_WATER = registerFluid(FLUIDS,"holy_water",7777, supplier -> new BurningLiquidBlock(supplier, FluidDeferredRegister.createProperties(MapColor.COLOR_GRAY, 15), 200, 8){
        @Override
        public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
            if (!(entity instanceof LivingEntity living)) return;
            if (isVampireOrUndead(living)) living.setHealth(living.getHealth()-1);

        }
    },false);

    private static FluidType.Properties hot(String name,int Temp,boolean gas) {
        return FluidType.Properties.create().density(gas?-2000:2000).viscosity(10000).temperature(Temp)
                .descriptionId("fluid."+ModMain.MODID+"."+name)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA)
                .motionScale(0.0023333333333333335D)
                .canSwim(false).canDrown(false)
                .pathType(BlockPathTypes.LAVA).adjacentPathType(null);
    }
}
