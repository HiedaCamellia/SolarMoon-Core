package cn.solarmoon.solarmoon_core.core.common.registry.ability;

import cn.solarmoon.solarmoon_core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.api.common.ability.BasicEntityBlockTicker;
import cn.solarmoon.solarmoon_core.api.common.block_entity.IContainerBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.ITankBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.IBlockEntityAnimateTicker;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.IIndividualTimeRecipeBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.ITimeRecipeBlockEntity;
import cn.solarmoon.solarmoon_core.api.util.namespace.SolarNBTList;
import cn.solarmoon.solarmoon_core.api.util.namespace.SolarNETList;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarNetPacks;
import net.minecraft.nbt.CompoundTag;


public class SolarTickers {
    public static void register() {}

    //防止放入物品未在客户端同步而造成的假右键操作

    public static final BasicEntityBlockTicker<?> IContainerSync = SolarMoonCore.REGISTRY
            .basicEntityBlockTicker(IContainerBlockEntity.class)
            .addSynchronizer((pair, nbt) -> {
                //物品容器同步
                nbt.put(SolarNBTList.INVENTORY, pair.getA().getInventory().serializeNBT());
                SolarNetPacks.CLIENT.getSender().send(SolarNETList.SYNC_IC_BLOCK, pair.getB().getBlockPos(), nbt);
            })
            .build();

    public static final BasicEntityBlockTicker<?> ITankSync = SolarMoonCore.REGISTRY
            .basicEntityBlockTicker(ITankBlockEntity.class)
            .addSynchronizer((pair, nbt) -> {
                //液体同步
                nbt.put(SolarNBTList.FLUID, pair.getA().getTank().writeToNBT(new CompoundTag()));
                SolarNetPacks.CLIENT.getSender().send(SolarNETList.SYNC_IT_BLOCK, pair.getB().getBlockPos(), nbt);
            })
            .build();

    public static final BasicEntityBlockTicker<?> ITimeRecipeSync = SolarMoonCore.REGISTRY
            .basicEntityBlockTicker(ITimeRecipeBlockEntity.class)
            .addSynchronizer((pair, nbt) -> {
                //配方时间同步
                nbt.putInt(SolarNBTList.TIME, pair.getA().getTime());
                SolarNetPacks.CLIENT.getSender().send(SolarNETList.SYNC_IRT_BLOCK, pair.getB().getBlockPos(), nbt);
            })
            .build();

    public static final BasicEntityBlockTicker<?> IIndividualTimeRecipeSync = SolarMoonCore.REGISTRY
            .basicEntityBlockTicker(IIndividualTimeRecipeBlockEntity.class)
            .addSynchronizer((pair, nbt) -> {
                //独立配方时间的同步
                nbt.putIntArray(SolarNBTList.SINGLE_STACK_TIME, pair.getA().getTimes());
                SolarNetPacks.CLIENT.getSender().send(SolarNETList.SYNC_IIRT_BLOCK, pair.getB().getBlockPos(), nbt);
            })
            .build();

    public static final BasicEntityBlockTicker<?> IAnimateTicker = SolarMoonCore.REGISTRY
            .basicEntityBlockTicker(IBlockEntityAnimateTicker.class)
            .add(pair -> {
                //动画计时器
                int ticks = pair.getA().getTicks();
                if (ticks <= 100) ticks++;
                else ticks = 0;
                pair.getA().setTicks(ticks);
            })
            .build();

}
