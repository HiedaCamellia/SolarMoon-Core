package cn.solarmoon.solarmoon_core.core.mixin;

import cn.solarmoon.solarmoon_core.api.common.ability.BlockEntityDataHolder;
import cn.solarmoon.solarmoon_core.api.common.block_entity.iutor.IBlockEntityAnimateTicker;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
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
        BlockEntityDataHolder.ALL.forEach(holder -> holder.getSaveConsumer().ifPresent(c -> c.accept(blockEntity, nbt)));
    }

    @Inject(method = "load", at = @At("HEAD"))
    public void load(CompoundTag nbt, CallbackInfo ci) {
        BlockEntityDataHolder.ALL.forEach(holder -> holder.getLoadConsumer().ifPresent(c -> c.accept(blockEntity, nbt)));
    }

    @SuppressWarnings("unchecked")
    @Override
    @NotNull
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        for (var holder : BlockEntityDataHolder.ALL) {
            var func = holder.getCapFunction();
            if (func.isPresent()) {
                LazyOptional<T> lazy = (LazyOptional<T>) func.get().apply(blockEntity, cap);
                if (lazy != null && lazy.isPresent()) { //必须检查，不然就直接return第一个了
                    return lazy;
                }
            }
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
