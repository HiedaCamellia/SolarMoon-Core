package cn.solarmoon.solarmoon_core.registry.object;

import cn.solarmoon.solarmoon_core.SolarMoonCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FluidEntry {

    private final DeferredRegister<Fluid> fluidRegister;
    private final DeferredRegister<FluidType> fluidTypeRegister;
    private final DeferredRegister<Item> itemRegister;
    private final DeferredRegister<Block> blockRegister;
    private final String modId;

    private ResourceLocation spriteStill;
    private ResourceLocation spriteFlowing;
    private String id;
    private Supplier<LiquidBlock> blockSupplier;
    private Supplier<FlowingFluid> stillSupplier;
    private Supplier<FlowingFluid> flowingSupplier;
    private Supplier<Item> bucketSupplier;
    private RegistryObject<LiquidBlock> fluidBlock;
    private RegistryObject<FlowingFluid> fluidFlowing;
    private RegistryObject<FlowingFluid> fluidStill;
    private RegistryObject<Item> fluidBucket;
    private RegistryObject<FluidType> fluidType;

    public FluidEntry(DeferredRegister<Fluid> fluidRegister, DeferredRegister<FluidType> fluidTypeRegister, DeferredRegister<Item> itemRegister, DeferredRegister<Block> blockRegister, String modId) {
        this.fluidRegister = fluidRegister;
        this.fluidTypeRegister = fluidTypeRegister;
        this.itemRegister = itemRegister;
        this.blockRegister = blockRegister;
        this.modId = modId;
    }

    public FluidEntry id(String id) {
        this.id = id;
        this.spriteStill = new ResourceLocation(modId + ":fluid/" + id + "_fluid_still");
        this.spriteFlowing = new ResourceLocation(modId + ":fluid/" + id + "_fluid_flowing");
        return this;
    }

    public FluidEntry bound(Supplier<LiquidBlock> blockSupplier) {
        this.blockSupplier = blockSupplier;
        return this;
    }

    public FluidEntry still(Supplier<FlowingFluid> stillSupplier) {
        this.stillSupplier = stillSupplier;
        return this;
    }

    public FluidEntry flowing(Supplier<FlowingFluid> flowingSupplier) {
        this.flowingSupplier = flowingSupplier;
        return this;
    }

    public FluidEntry bucket(Supplier<Item> bucketSupplier) {
        this.bucketSupplier = bucketSupplier;
        return this;
    }

    public FluidEntry build() {
        fluidBlock = blockRegister.register(id, blockSupplier);
        fluidStill = fluidRegister.register(id, stillSupplier);
        fluidFlowing = fluidRegister.register(id + "_flowing", flowingSupplier);
        if (bucketSupplier != null) fluidBucket = itemRegister.register(id + "_bucket", bucketSupplier);
        fluidType = fluidTypeRegister.register(id, () -> new FluidType(FluidType.Properties.create()) {

            @Override
            public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                consumer.accept(new IClientFluidTypeExtensions() {

                    @Override
                    public ResourceLocation getStillTexture() {
                        return spriteStill;
                    }

                    @Override
                    public ResourceLocation getFlowingTexture() {
                        return spriteFlowing;
                    }

                    @Override
                    public ResourceLocation getOverlayTexture() {
                        return null;
                    }

                });
            }
        });
        return this;
    }

    public LiquidBlock getBlock() {
        return fluidBlock.get();
    }

    public FlowingFluid getStill() {
        return fluidStill.get();
    }

    public FlowingFluid getFlowing() {
        return fluidFlowing.get();
    }

    public Item getBucket() {
        return fluidBucket.get();
    }

    public FluidType getType() {
        return fluidType.get();
    }

    public RegistryObject<LiquidBlock> getBlockObject() {
        return fluidBlock;
    }

    public RegistryObject<FlowingFluid> getStillObject() {
        return fluidStill;
    }

    public RegistryObject<FlowingFluid> getFlowingObject() {
        return fluidFlowing;
    }

    public RegistryObject<Item> getBucketObject() {
        return fluidBucket;
    }

    public RegistryObject<FluidType> getTypeObject() {
        return fluidType;
    }

}
