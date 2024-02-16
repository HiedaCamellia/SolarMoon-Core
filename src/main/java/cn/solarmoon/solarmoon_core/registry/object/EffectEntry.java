package cn.solarmoon.solarmoon_core.registry.object;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class EffectEntry {

    private final DeferredRegister<MobEffect> effectRegister;

    private String id;
    private Supplier<MobEffect> effectSupplier;
    private RegistryObject<MobEffect> effectObject;

    public EffectEntry(DeferredRegister<MobEffect> effectRegister) {
        this.effectRegister = effectRegister;
    }

    public EffectEntry id(String id) {
        this.id = id;
        return this;
    }

    public EffectEntry bound(Supplier<MobEffect> effectSupplier) {
        this.effectSupplier = effectSupplier;
        return this;
    }

    public EffectEntry build() {
        this.effectObject = effectRegister.register(id, effectSupplier);
        return this;
    }

    public MobEffect get() {
        return effectObject.get();
    }

}
