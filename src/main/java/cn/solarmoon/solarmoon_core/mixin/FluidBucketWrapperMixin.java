package cn.solarmoon.solarmoon_core.mixin;

import cn.solarmoon.solarmoon_core.feature.bucket_fix.BucketFixUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FluidBucketWrapper.class)
public class FluidBucketWrapperMixin {

    @Shadow @NotNull protected ItemStack container;

    @Inject(remap = false, method = "getFluid", at = @At("RETURN"), cancellable = true)
    public void getFluid(CallbackInfoReturnable<FluidStack> cir) {
        FluidStack fluidStack = cir.getReturnValue();
        if (fluidStack != null) {
            BucketFixUtil.readBucketTagToFluid(container, fluidStack);
            cir.setReturnValue(fluidStack);
        }
    }

}
