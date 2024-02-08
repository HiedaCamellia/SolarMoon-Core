package cn.solarmoon.solarmoon_core.network.handler;


import cn.solarmoon.solarmoon_core.common.entity_block.entity.BaseContainerBlockEntity;
import cn.solarmoon.solarmoon_core.common.entity_block.entity.BaseTCBlockEntity;
import cn.solarmoon.solarmoon_core.common.entity_block.entity.BaseTankBlockEntity;
import cn.solarmoon.solarmoon_core.network.IClientPackHandler;
import cn.solarmoon.solarmoon_core.util.namespace.NETList;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class BaseClientPackHandler implements IClientPackHandler {

    @Override
    public void handle(LocalPlayer player, ClientLevel level, BlockPos pos, CompoundTag tag, float f, String string, List<ItemStack> stacks, String message) {
        switch (message) {
            case NETList.SYNC_C_BLOCK -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if(blockEntity instanceof BaseContainerBlockEntity c) {
                    c.setInventory(tag);
                }
            }
            case NETList.SYNC_T_BLOCK -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if(blockEntity instanceof BaseTankBlockEntity tankBlockEntity) {
                    tankBlockEntity.setFluid(tag);
                }
            }
            case NETList.SYNC_TC_BLOCK -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if(blockEntity instanceof BaseTCBlockEntity tc) {
                    tc.setFluid(tag);
                    tc.setInventory(tag);
                }
            }
        }
    }

}
