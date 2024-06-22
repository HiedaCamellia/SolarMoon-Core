package cn.solarmoon.solarmoon_core.network;


import cn.solarmoon.solarmoon_core.api.network.IClientPackHandler;
import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class BaseClientPackHandler implements IClientPackHandler {

    @Override
    public void handle(LocalPlayer player, ClientLevel level, BlockPos pos, ItemStack stack, CompoundTag tag, FluidStack fluidStack, float f, int[] ints, String string, List<ItemStack> stacks, List<Vec3> vec3List, boolean flag, int i, String message) {
        switch (message) {
            case NETList.SYNC_BLOCK_ENTITY -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity != null) {
                    blockEntity.deserializeNBT(tag);
                }
            }
            case NETList.SYNC_FURNACE -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof AbstractFurnaceBlockEntity e) {
                    for(int n = 0; n < stacks.size(); n++) {
                        e.setItem(n, stacks.get(n));
                    }
                }
            }
            case NETList.PARTICLE_SWEEP -> {
                Vec3 spawnPos = vec3List.get(0);
                level.addParticle(ParticleTypes.SWEEP_ATTACK, spawnPos.x, spawnPos.y-0.35, spawnPos.z, 0, 0, 0);
            }
            case NETList.SYNC_ANIM -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity == null) return;
                blockEntity.getCapability(SolarCapabilities.BLOCK_ENTITY_DATA).ifPresent(d -> {
                    d.getAnimTicker(i).deserializeNBT(tag);
                });
            }
        }
    }

}
