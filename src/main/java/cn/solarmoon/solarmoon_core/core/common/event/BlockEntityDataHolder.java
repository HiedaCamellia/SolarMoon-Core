package cn.solarmoon.solarmoon_core.core.common.event;

import cn.solarmoon.solarmoon_core.api.common.block_entity.IContainerBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.ITankBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.IIndividualTimeRecipeBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.ITimeRecipeBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.event.BlockEntityDataEvent;
import cn.solarmoon.solarmoon_core.api.util.namespace.SolarNBTList;
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
        if (be instanceof IContainerBlockEntity c) {
            tag.put(SolarNBTList.INVENTORY, c.getInventory().serializeNBT());
        }
        if (be instanceof ITankBlockEntity t) {
            CompoundTag fluid = new CompoundTag();
            t.getTank().writeToNBT(fluid);
            tag.put(SolarNBTList.FLUID, fluid);
        }
        if (be instanceof ITimeRecipeBlockEntity<?> time) {
            tag.putInt(SolarNBTList.TIME, time.getTime());
        }
        if (be instanceof IIndividualTimeRecipeBlockEntity<?> in) {
            tag.putIntArray(SolarNBTList.SINGLE_STACK_TIME, in.getTimes());
        }
    }

    @SubscribeEvent
    public void load(BlockEntityDataEvent.Load event) {
        BlockEntity be = event.getBlockEntity();
        CompoundTag tag = event.getTag();
        if (be instanceof IContainerBlockEntity c) {
            c.getInventory().deserializeNBT(tag.getCompound(SolarNBTList.INVENTORY));
        }
        if (be instanceof ITankBlockEntity t) {
            t.getTank().readFromNBT(tag.getCompound(SolarNBTList.FLUID));
        }
        if (be instanceof ITimeRecipeBlockEntity<?> time) {
            time.setTime(tag.getInt(SolarNBTList.TIME));
        }
        if (be instanceof IIndividualTimeRecipeBlockEntity<?> in) {
            int[] getFrom = tag.getIntArray(SolarNBTList.SINGLE_STACK_TIME);
            if (getFrom.length != 0) {
                in.setTimes(getFrom);
            }
        }
    }

    @SubscribeEvent
    public void capability(BlockEntityDataEvent.Capability event) {
        BlockEntity be = event.getBlockEntity();
        var cap = event.getCap();
        if (be instanceof IContainerBlockEntity c) {
            if (cap == ForgeCapabilities.ITEM_HANDLER) {
                event.setReturnValue(LazyOptional.of(c::getInventory));
            }
        }
        if (be instanceof ITankBlockEntity t) {
            if (cap == ForgeCapabilities.FLUID_HANDLER) {
                event.setReturnValue(LazyOptional.of(t::getTank));
            }
        }
    }

}
