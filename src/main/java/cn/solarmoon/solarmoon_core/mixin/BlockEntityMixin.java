package cn.solarmoon.solarmoon_core.mixin;

import cn.solarmoon.solarmoon_core.common.entity_block.entity.IContainerBlockEntity;
import cn.solarmoon.solarmoon_core.common.entity_block.entity.ITankBlockEntity;
import cn.solarmoon.solarmoon_core.common.entity_block.entity.iutor.IBlockEntityAnimateTicker;
import cn.solarmoon.solarmoon_core.util.namespace.SolarNBTList;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public class BlockEntityMixin extends CapabilityProvider<BlockEntity> {

    private final BlockEntity blockEntity = (BlockEntity) (Object) this;

    protected BlockEntityMixin(Class<BlockEntity> baseClass) {
        super(baseClass);
    }

    @Inject(method = "saveAdditional", at = @At("HEAD"))
    public void saveAdditional(CompoundTag nbt, CallbackInfo ci) {
        if (blockEntity instanceof IContainerBlockEntity containerTile) {
            nbt.put(SolarNBTList.INVENTORY, containerTile.getInventory().serializeNBT());
        }
        if (blockEntity instanceof ITankBlockEntity tankTile) {
            CompoundTag fluid = new CompoundTag();
            tankTile.getTank().writeToNBT(fluid);
            nbt.put(SolarNBTList.FLUID, fluid);
            blockEntity.setChanged();
        }
    }

    @Inject(method = "load", at = @At("HEAD"))
    public void load(CompoundTag nbt, CallbackInfo ci) {
        if (blockEntity instanceof IContainerBlockEntity containerTile) {
            containerTile.getInventory().deserializeNBT(nbt.getCompound(SolarNBTList.INVENTORY));
        }
        if (blockEntity instanceof ITankBlockEntity tankTile) {
            tankTile.getTank().readFromNBT(nbt.getCompound(SolarNBTList.FLUID));
        }
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER && blockEntity instanceof IContainerBlockEntity containerTile) {
            return LazyOptional.of(containerTile::getInventory).cast();
        }
        if (cap == ForgeCapabilities.FLUID_HANDLER && blockEntity instanceof ITankBlockEntity tankTile) {
            return LazyOptional.of(tankTile::getTank).cast();
        }
        return super.getCapability(cap, side);
    }

    @Inject(method = "setChanged()V", at = @At("HEAD"))
    public void setChanged(CallbackInfo ci) {
        if (blockEntity instanceof IBlockEntityAnimateTicker ticker) {
            ticker.setTicks(0);
        }
    }

}
