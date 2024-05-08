package cn.solarmoon.solarmoon_core.core.common.registry.ability;

import cn.solarmoon.solarmoon_core.core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.api.common.ability.BlockEntityDataHolder;
import cn.solarmoon.solarmoon_core.api.common.block_entity.IContainerBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.ITankBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.IIndividualTimeRecipeBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.ITimeRecipeBlockEntity;
import cn.solarmoon.solarmoon_core.api.util.namespace.SolarNBTList;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;

public class SolarTileDataHolders {
    public static void register() {}

    public static final BlockEntityDataHolder<?> IContainerDataHolder = SolarMoonCore.REGISTRY
            .blockEntityDataHolder(IContainerBlockEntity.class)
            .save((containerTile, nbt) -> nbt.put(SolarNBTList.INVENTORY, containerTile.getInventory().serializeNBT()))
            .load((containerTile, nbt) -> containerTile.getInventory().deserializeNBT(nbt.getCompound(SolarNBTList.INVENTORY)))
            .capability((containerTile, cap) -> {
                if (cap == ForgeCapabilities.ITEM_HANDLER) {
                    return LazyOptional.of(containerTile::getInventory);
                }
                return null;
            })
            .build();

    public static final BlockEntityDataHolder<?> ITankDataHolder = SolarMoonCore.REGISTRY
            .blockEntityDataHolder(ITankBlockEntity.class)
            .save((tankTile, nbt) -> {
                CompoundTag fluid = new CompoundTag();
                tankTile.getTank().writeToNBT(fluid);
                nbt.put(SolarNBTList.FLUID, fluid);
            })
            .load((tankTile, nbt) -> tankTile.getTank().readFromNBT(nbt.getCompound(SolarNBTList.FLUID)))
            .capability((tankTile, cap) -> {
                if (cap == ForgeCapabilities.FLUID_HANDLER) {
                    return LazyOptional.of(tankTile::getTank);
                }
                return null;
            })
            .build();

    public static final BlockEntityDataHolder<?> ITimeRecipeDataHolder = SolarMoonCore.REGISTRY
            .blockEntityDataHolder(ITimeRecipeBlockEntity.class)
            .save((timeTile, nbt) -> nbt.putInt(SolarNBTList.TIME, timeTile.getTime()))
            .load((timeTile, nbt) -> timeTile.setTime(nbt.getInt(SolarNBTList.TIME)))
            .build();

    public static final BlockEntityDataHolder<?> IIndividualTimeRecipeDataHolder = SolarMoonCore.REGISTRY
            .blockEntityDataHolder(IIndividualTimeRecipeBlockEntity.class)
            .save((timesTile, nbt) -> nbt.putIntArray(SolarNBTList.SINGLE_STACK_TIME, timesTile.getTimes()))
            .load((timesTile, nbt) -> {
                int[] getFrom = nbt.getIntArray(SolarNBTList.SINGLE_STACK_TIME);
                if (getFrom.length != 0) {
                    timesTile.setTimes(getFrom);
                }
            })
            .build();

}
