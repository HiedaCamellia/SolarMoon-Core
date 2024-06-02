package cn.solarmoon.solarmoon_core.core.mixin;

import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.IBlockEntityAnimateTicker;
import cn.solarmoon.solarmoon_core.api.common.capability.IBlockEntityData;
import cn.solarmoon.solarmoon_core.api.common.event.BlockEntityDataEvent;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityProvider;
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
        BlockEntityDataEvent.Save saveEvent = new BlockEntityDataEvent.Save(nbt, blockEntity);
        MinecraftForge.EVENT_BUS.post(saveEvent);
    }

    @Inject(method = "load", at = @At("HEAD"))
    public void load(CompoundTag nbt, CallbackInfo ci) {
        BlockEntityDataEvent.Load loadEvent = new BlockEntityDataEvent.Load(nbt, blockEntity);
        MinecraftForge.EVENT_BUS.post(loadEvent);
    }

    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        BlockEntityDataEvent.Capability event = new BlockEntityDataEvent.Capability(blockEntity, cap, side, super.getCapability(cap, side));
        MinecraftForge.EVENT_BUS.post(event);
        return event.getReturnValue();
    }

    @Inject(method = "setChanged()V", at = @At("HEAD"))
    public void setChanged(CallbackInfo ci) {
        if (blockEntity instanceof IBlockEntityAnimateTicker ticker) {
            ticker.setTicks(0);
        }
        blockEntity.getCapability(SolarCapabilities.BLOCK_ENTITY_DATA).ifPresent(data -> data.getAnimTicker().start());
    }

}
