package cn.solarmoon.solarmoon_core.registry.object;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

public class DamageTypeEntry {

    private final String modId;

    private String id;
    private ResourceKey<DamageType> damageTypeResourceKey;

    public DamageTypeEntry(String modId) {
        this.modId = modId;
    }

    public DamageTypeEntry id(String id) {
        this.id = id;
        return this;
    }

    public DamageTypeEntry build() {
        this.damageTypeResourceKey = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(modId, id));
        return this;
    }

    public DamageSource getSimple(Level level) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageTypeResourceKey));
    }

}
