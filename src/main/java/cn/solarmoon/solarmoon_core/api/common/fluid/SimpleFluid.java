package cn.solarmoon.solarmoon_core.api.common.fluid;

import cn.solarmoon.solarmoon_core.api.common.registry.FluidEntry;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class SimpleFluid {

    private final FluidEntry fluidEntry;

    public SimpleFluid(FluidEntry fluidEntry) {
        this.fluidEntry = fluidEntry;
    }

    public class FluidBlock extends BaseFluid.FluidBlock {
        public FluidBlock() {
            super(fluidEntry.getStillObject());
        }
    }

    public class Flowing extends BaseFluid.Flowing {
        public Flowing() {
            super(makeProperties(fluidEntry));
        }
    }

    public class Source extends BaseFluid.Source {
        public Source() {
            super(makeProperties(fluidEntry));
        }
    }

    public class Bucket extends BaseFluid.Bucket {
        public Bucket() {
            super(fluidEntry.getStillObject());
        }
    }

    private ForgeFlowingFluid.Properties makeProperties(FluidEntry fluidEntry) {
        return new ForgeFlowingFluid.Properties(
                fluidEntry.getTypeObject(),
                fluidEntry.getStillObject(),
                fluidEntry.getFlowingObject()
        )
                .block(fluidEntry.getBlockObject())
                .bucket(fluidEntry.getBucketObject());
    }

}
