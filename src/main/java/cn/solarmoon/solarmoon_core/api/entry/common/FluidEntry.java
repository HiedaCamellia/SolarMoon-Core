package cn.solarmoon.solarmoon_core.api.entry.common;

import net.minecraft.client.Minecraft;
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
    private ResourceLocation spriteOverlay;
    private boolean defaultSprite;
    private int color;
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
        this.defaultSprite = false;
        this.color = 0xFFFFFFFF;
    }

    public FluidEntry id(String id) {
        this.id = id;
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

    public FluidEntry waterLike() {
        this.defaultSprite = true;
        return this;
    }

    public FluidEntry color(int color) {
        this.color = color;
        return this;
    }

    public FluidEntry build() {
        fluidBlock = blockRegister.register(id, blockSupplier);
        fluidStill = fluidRegister.register(id, stillSupplier);
        fluidFlowing = fluidRegister.register(id + "_flowing", flowingSupplier);
        if (bucketSupplier != null) fluidBucket = itemRegister.register(id + "_bucket", bucketSupplier);
        if (defaultSprite) {
            this.spriteStill = new ResourceLocation("minecraft:block/water" + "_still");
            this.spriteFlowing = new ResourceLocation("minecraft:block/water" + "_flow");
            this.spriteOverlay = new ResourceLocation(modId + ":textures/misc/" + id + "_under.png");
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
                        public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                            return spriteOverlay;
                        }

                        @Override
                        public int getTintColor() {
                            return color;
                        }
                    });
                }
            });
        } else {
            this.spriteStill = new ResourceLocation(modId + ":block/fluid/" + id + "_still");
            this.spriteFlowing = new ResourceLocation(modId + ":block/fluid/" + id + "_flow");
            this.spriteOverlay = new ResourceLocation(modId + ":textures/misc/" + id + "_under.png");
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
                        public ResourceLocation getRenderOverlayTexture(Minecraft mc) {
                            return spriteOverlay;
                        }

                        @Override
                        public int getTintColor() {
                            return color;
                        }
                    });
                }
            });
        }
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
