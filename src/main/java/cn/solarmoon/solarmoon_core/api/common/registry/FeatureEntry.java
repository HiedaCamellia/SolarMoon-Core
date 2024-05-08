package cn.solarmoon.solarmoon_core.api.common.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FeatureEntry<F extends FeatureConfiguration> {

    private final DeferredRegister<Feature<?>> featureRegister;
    private final String modId;

    private String id;
    private ResourceKey<PlacedFeature> placedKey;
    private ResourceKey<ConfiguredFeature<?, ?>> configKey;
    private Supplier<Feature<?>> featureSupplier;
    private Supplier<F> config;
    private Supplier<List<PlacementModifier>> placement;
    private RegistryObject<Feature<?>> featureObject;

    private static final List<FeatureEntry<?>> featureEntries = new ArrayList<>();

    public FeatureEntry(DeferredRegister<Feature<?>> featureRegister, String modId) {
        this.featureRegister = featureRegister;
        this.modId = modId;
    }

    public FeatureEntry<F> id(String id) {
        this.id = id;
        this.placedKey = ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(modId, id));
        this.configKey = ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(modId, id));
        return this;
    }

    public FeatureEntry<F> bound(Supplier<Feature<?>> featureSupplier) {
        this.featureSupplier = featureSupplier;
        return this;
    }

    public FeatureEntry<F> config(Supplier<F> config) {
        this.config = config;
        return this;
    }

    public FeatureEntry<F> placement(Supplier<List<PlacementModifier>> placement) {
        this.placement = placement;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <FC extends FeatureConfiguration> FeatureEntry<FC> build() {
        this.featureObject = featureRegister.register(id, featureSupplier);
        featureEntries.add(this);
        return (FeatureEntry<FC>) this;
    }

    @SuppressWarnings("unchecked")
    public Feature<F> get() {
        return (Feature<F>) featureObject.get();
    }

    public ResourceKey<ConfiguredFeature<?, ?>> getConfigKey() {
        return configKey;
    }

    public ResourceKey<PlacedFeature> getPlacementKey() {
        return placedKey;
    }

    public F getConfig() {
        return config.get();
    }

    public List<PlacementModifier> getPlacement() {
        return placement.get();
    }

    @SuppressWarnings("unchecked")
    public static <FC extends FeatureConfiguration, F extends Feature<FC>> void configBootStrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        for (var feature : FeatureEntry.featureEntries) {
            context.register(feature.getConfigKey(), new ConfiguredFeature<>((F)feature.get(), (FC) feature.getConfig()));
        }
    }

    public static void placedBootStrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> featureGetter = context.lookup(Registries.CONFIGURED_FEATURE);
        for (var feature : FeatureEntry.featureEntries) {
            Holder<ConfiguredFeature<?, ?>> holder = featureGetter.getOrThrow(feature.getConfigKey());
            context.register(feature.getPlacementKey(), new PlacedFeature(holder, feature.getPlacement()));
        }
    }

}
