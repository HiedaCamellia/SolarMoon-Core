package cn.solarmoon.solarmoon_core.api.blockentity_util;

import cn.solarmoon.solarmoon_core.api.event.BlockEntityDataEvent;
import cn.solarmoon.solarmoon_core.api.util.ContainerUtil;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockEntityDataHolder {

    @SubscribeEvent
    public void save(BlockEntityDataEvent.Save event) {
        BlockEntity be = event.getBlockEntity();
        CompoundTag tag = event.getTag();
        if (be instanceof IContainerBE c) {
            tag.put(ContainerUtil.INVENTORY, c.getInventory().serializeNBT());
        }
        if (be instanceof ITankBE t) {
            CompoundTag fluid = new CompoundTag();
            t.getTank().writeToNBT(fluid);
            tag.put(FluidUtil.FLUID, fluid);
        }
        if (be instanceof ITimeRecipeBE<?> time) {
            tag.putInt(ITimeRecipeBE.TIME, time.getTime());
        }
        if (be instanceof IIndividualTimeRecipeBE<?> in) {
            tag.putIntArray(IIndividualTimeRecipeBE.SINGLE_STACK_TIME, in.getTimes());
        }
    }

    @SubscribeEvent
    public void load(BlockEntityDataEvent.Load event) {
        BlockEntity be = event.getBlockEntity();
        CompoundTag tag = event.getTag();
        if (be instanceof IContainerBE c) {
            c.getInventory().deserializeNBT(tag.getCompound(ContainerUtil.INVENTORY));
        }
        if (be instanceof ITankBE t) {
            t.getTank().readFromNBT(tag.getCompound(FluidUtil.FLUID));
        }
        if (be instanceof ITimeRecipeBE<?> time) {
            time.setTime(tag.getInt(ITimeRecipeBE.TIME));
        }
        if (be instanceof IIndividualTimeRecipeBE<?> in) {
            int[] getFrom = tag.getIntArray(IIndividualTimeRecipeBE.SINGLE_STACK_TIME);
            if (getFrom.length != 0) {
                in.setTimes(getFrom);
            }
        }
    }

    @SubscribeEvent
    public void capability(BlockEntityDataEvent.Capability event) {
        BlockEntity be = event.getBlockEntity();
        var cap = event.getCap();
        if (be instanceof IContainerBE c) {
            if (cap == ForgeCapabilities.ITEM_HANDLER) {
                event.setReturnValue(LazyOptional.of(c::getInventory));
            }
        }
        if (be instanceof ITankBE t) {
            if (cap == ForgeCapabilities.FLUID_HANDLER) {
                event.setReturnValue(LazyOptional.of(t::getTank));
            }
        }
    }

}
