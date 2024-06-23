package cn.solarmoon.solarmoon_core.api.tile;

import cn.solarmoon.solarmoon_core.api.event.BlockEntityDataEvent;
import cn.solarmoon.solarmoon_core.api.tile.fluid.ITankTile;
import cn.solarmoon.solarmoon_core.api.tile.inventory.IContainerTile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class BlockEntityDataHolder {

    @SubscribeEvent
    public void save(BlockEntityDataEvent.Save event) {
        BlockEntity be = event.getBlockEntity();
        CompoundTag tag = event.getTag();
        if (be instanceof IContainerTile c) {
            tag.put(IContainerTile.INVENTORY, c.getInventory().serializeNBT());
        }
        if (be instanceof ITankTile t) {
            CompoundTag fluid = new CompoundTag();
            t.getTank().writeToNBT(fluid);
            tag.put(FluidHandlerItemStack.FLUID_NBT_KEY, fluid);
        }
        if (be instanceof ITimeRecipeTile<?> time) {
            tag.putInt(ITimeRecipeTile.TIME, time.getTime());
        }
        if (be instanceof IIndividualTimeRecipeTile<?> in) {
            tag.putIntArray(IIndividualTimeRecipeTile.SINGLE_STACK_TIME, in.getTimes());
        }
    }

    @SubscribeEvent
    public void load(BlockEntityDataEvent.Load event) {
        BlockEntity be = event.getBlockEntity();
        CompoundTag tag = event.getTag();
        if (be instanceof IContainerTile c) {
            c.getInventory().deserializeNBT(tag.getCompound(IContainerTile.INVENTORY));
        }
        if (be instanceof ITankTile t) {
            t.getTank().readFromNBT(tag.getCompound(FluidHandlerItemStack.FLUID_NBT_KEY));
        }
        if (be instanceof ITimeRecipeTile<?> time) {
            time.setTime(tag.getInt(ITimeRecipeTile.TIME));
        }
        if (be instanceof IIndividualTimeRecipeTile<?> in) {
            int[] getFrom = tag.getIntArray(IIndividualTimeRecipeTile.SINGLE_STACK_TIME);
            if (getFrom.length != 0) {
                in.setTimes(getFrom);
            }
        }
    }

    @SubscribeEvent
    public void capability(BlockEntityDataEvent.Capability event) {
        BlockEntity be = event.getBlockEntity();
        var cap = event.getCap();
        if (be instanceof IContainerTile c) {
            if (cap == ForgeCapabilities.ITEM_HANDLER) {
                event.setReturnValue(LazyOptional.of(c::getInventory));
            }
        }
        if (be instanceof ITankTile t) {
            if (cap == ForgeCapabilities.FLUID_HANDLER) {
                event.setReturnValue(LazyOptional.of(t::getTank));
            }
        }
    }

}
