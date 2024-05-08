package cn.solarmoon.solarmoon_core.core.network;


import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.IIndividualTimeRecipeBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.IContainerBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.ITankBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.ITimeRecipeBlockEntity;
import cn.solarmoon.solarmoon_core.api.network.IClientPackHandler;
import cn.solarmoon.solarmoon_core.api.util.namespace.SolarNBTList;
import cn.solarmoon.solarmoon_core.api.util.namespace.SolarNETList;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class BaseClientPackHandler implements IClientPackHandler {

    @Override
    public void handle(LocalPlayer player, ClientLevel level, BlockPos pos, ItemStack stack, CompoundTag tag, float f, int[] ints, String string, List<ItemStack> stacks, String message) {
        switch (message) {
            case SolarNETList.SYNC_FURNACE -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof AbstractFurnaceBlockEntity e) {
                    for(int i = 0; i < stacks.size(); i++) {
                        e.setItem(i, stacks.get(i));
                    }
                }
            }
            case SolarNETList.SYNC_IC_BLOCK -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if(blockEntity instanceof IContainerBlockEntity containerTile) {
                    containerTile.setInventory(tag);
                }
            }
            case SolarNETList.SYNC_IT_BLOCK -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if(blockEntity instanceof ITankBlockEntity tankTile) {
                    tankTile.setFluid(tag);
                }
            }
            case SolarNETList.SYNC_IRT_BLOCK -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof ITimeRecipeBlockEntity<?> timeTile) {
                    timeTile.setTime(tag.getInt(SolarNBTList.TIME));
                }
            }
            case SolarNETList.SYNC_IIRT_BLOCK -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof IIndividualTimeRecipeBlockEntity<?> timesTile) {
                    timesTile.setTimes(tag.getIntArray(SolarNBTList.SINGLE_STACK_TIME));
                }
            }
        }
    }

}
