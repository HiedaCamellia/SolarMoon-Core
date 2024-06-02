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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class BaseClientPackHandler implements IClientPackHandler {

    @Override
    public void handle(LocalPlayer player, ClientLevel level, BlockPos pos, ItemStack stack, CompoundTag tag, FluidStack fluidStack, float f, int[] ints, String string, List<ItemStack> stacks, List<Vec3> vec3List, boolean flag, String message) {
        switch (message) {
            case SolarNETList.SYNC_BLOCK_ENTITY -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity != null) {
                    blockEntity.deserializeNBT(tag);
                }
            }
            case SolarNETList.SYNC_FURNACE -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof AbstractFurnaceBlockEntity e) {
                    for(int i = 0; i < stacks.size(); i++) {
                        e.setItem(i, stacks.get(i));
                    }
                }
            }
            case SolarNETList.PARTICLE_SWEEP -> {
                Vec3 spawnPos = vec3List.get(0);
                level.addParticle(ParticleTypes.SWEEP_ATTACK, spawnPos.x, spawnPos.y-0.35, spawnPos.z, 0, 0, 0);
            }
        }
    }

}
