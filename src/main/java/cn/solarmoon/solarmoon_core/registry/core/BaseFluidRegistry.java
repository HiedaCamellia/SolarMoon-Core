package cn.solarmoon.solarmoon_core.registry.core;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;

/**
 * 基本的液体注册表（其实还没实现，为了强行统一先这样吧）
 */
public abstract class BaseFluidRegistry {

    protected DeferredRegister<Fluid> FLUIDS;
    protected DeferredRegister<FluidType> FLUID_TYPES;

    protected List<Object> objects;

    public BaseFluidRegistry(DeferredRegister<Fluid> FLUIDS, DeferredRegister<FluidType> FLUID_TYPES) {
        this.FLUIDS = FLUIDS;
        this.FLUID_TYPES = FLUID_TYPES;
        this.objects = new ArrayList<>();
    }

    /**
     * 使用objects.add为每种液体类添加注册表
     */
    void addRegistry() {

    }

    public void register(IEventBus bus) {
        new Core().register(bus);
        new Type().register(bus);
    }

    public class Core extends BaseObjectRegistry<Fluid> {
        public Core() {
            super(FLUIDS);
        }
    }

    public class Type extends BaseObjectRegistry<FluidType> {
        public Type() {
            super(FLUID_TYPES);
        }
    }

}
