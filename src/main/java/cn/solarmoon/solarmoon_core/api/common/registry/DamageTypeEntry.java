package cn.solarmoon.solarmoon_core.api.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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

    public DamageSource get(Level level) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageTypeResourceKey));
    }

    public DamageSource get(Level level, Entity directEntity) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageTypeResourceKey),
                directEntity);
    }

    public DamageSource get(Level level, Entity directEntity, Entity causingEntity) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageTypeResourceKey),
                directEntity, causingEntity);
    }

    public DamageSource get(Level level, Entity directEntity, Entity causingEntity, Vec3 position) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageTypeResourceKey),
                directEntity, causingEntity, position);
    }

}
