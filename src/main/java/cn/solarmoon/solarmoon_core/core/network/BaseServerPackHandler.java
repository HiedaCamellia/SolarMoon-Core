package cn.solarmoon.solarmoon_core.core.network;

import cn.solarmoon.solarmoon_core.api.network.IServerPackHandler;
import cn.solarmoon.solarmoon_core.api.util.LevelSummonUtil;
import cn.solarmoon.solarmoon_core.api.util.namespace.SolarNETList;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class BaseServerPackHandler implements IServerPackHandler {
    @Override
    public void handle(ServerPlayer player, ServerLevel level, BlockPos pos, ItemStack stack, CompoundTag tag, FluidStack fluidStack, float f, int[] ints, String string, List<ItemStack> stacks, List<Vec3> vec3List, boolean flag, String message) {
        switch (message) {
            case SolarNETList.PUMP -> {
                LevelSummonUtil.summonDrop(stacks, level, pos.getCenter().add(vec3List.get(0)));
            }
            case SolarNETList.SYNC_ANIM_FIXED_TICK -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity != null) {
                    blockEntity.getCapability(SolarCapabilities.BLOCK_ENTITY_DATA).ifPresent(data -> data.getAnimTicker().setFixedValue(f));
                }
            }
            case SolarNETList.SYNC_ANIM_FLUID -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity != null) {
                    blockEntity.getCapability(SolarCapabilities.BLOCK_ENTITY_DATA).ifPresent(data -> data.getAnimTicker().setFixedFluid(fluidStack));
                }
            }
            case SolarNETList.SYNC_ANIM_TICK -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity != null) {
                    blockEntity.getCapability(SolarCapabilities.BLOCK_ENTITY_DATA).ifPresent(data -> data.getAnimTicker().setTicks((int) f));
                }
            }
            case SolarNETList.SYNC_ANIM_MAX_TICK -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity != null) {
                    blockEntity.getCapability(SolarCapabilities.BLOCK_ENTITY_DATA).ifPresent(data -> data.getAnimTicker().setMaxTick((int) f));
                }
            }
            case SolarNETList.SYNC_ANIM_ENABLED -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity != null) {
                    blockEntity.getCapability(SolarCapabilities.BLOCK_ENTITY_DATA).ifPresent(data -> data.getAnimTicker().setEnabled(flag));
                }
            }
        }
    }
}
