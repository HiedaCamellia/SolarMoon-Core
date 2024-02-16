package cn.solarmoon.solarmoon_core.registry.core;

import cn.solarmoon.solarmoon_core.registry.object.*;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ObjectRegistry {

    private final String modId;
    public DeferredRegister<Item> itemRegister;
    public DeferredRegister<Block> blockRegister;
    public DeferredRegister<Fluid> fluidRegister;
    public DeferredRegister<FluidType> fluidTypeRegister;
    public DeferredRegister<BlockEntityType<?>> blockEntityRegister;
    public DeferredRegister<EntityType<?>> entityRegister;
    public DeferredRegister<RecipeType<?>> recipeTypeRegister;
    public DeferredRegister<RecipeSerializer<?>> recipeSerializerRegister;
    public DeferredRegister<MobEffect> effectRegister;
    public DeferredRegister<ParticleType<?>> particleTypeRegister;
    public DeferredRegister<SoundEvent> soundRegister;
    public DeferredRegister<Feature<?>> featureRegister;
    public DeferredRegister<CreativeModeTab> creativeTabRegister;

    public ObjectRegistry(String modId) {
        this.modId = modId;
        this.itemRegister = DeferredRegister.create(ForgeRegistries.ITEMS, modId);
        this.blockRegister = DeferredRegister.create(ForgeRegistries.BLOCKS, modId);
        this.fluidRegister = DeferredRegister.create(ForgeRegistries.FLUIDS, modId);
        this.fluidTypeRegister = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, modId);
        this.blockEntityRegister = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, modId);
        this.entityRegister = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, modId);
        this.recipeTypeRegister = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, modId);
        this.recipeSerializerRegister = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, modId);
        this.effectRegister = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, modId);
        this.particleTypeRegister = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, modId);
        this.soundRegister = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, modId);
        this.featureRegister = DeferredRegister.create(ForgeRegistries.FEATURES, modId);
        this.creativeTabRegister = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, modId);
        register();
    }

    public void register() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        this.itemRegister.register(bus);
        this.blockRegister.register(bus);
        this.fluidRegister.register(bus);
        this.fluidTypeRegister.register(bus);
        this.blockEntityRegister.register(bus);
        this.entityRegister.register(bus);
        this.recipeTypeRegister.register(bus);
        this.recipeSerializerRegister.register(bus);
        this.effectRegister.register(bus);
        this.particleTypeRegister.register(bus);
        this.soundRegister.register(bus);
        this.featureRegister.register(bus);
        this.creativeTabRegister.register(bus);
        bus.addListener(this::gatherData);
    }

    @SubscribeEvent
    public void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        RegistrySetBuilder builder = new RegistrySetBuilder();
        builder.add(Registries.CONFIGURED_FEATURE, FeatureEntry::configBootStrap);
        builder.add(Registries.PLACED_FEATURE, FeatureEntry::placedBootStrap);
        generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(output, lookupProvider, builder, Set.of(modId)));
    }

    public ObjectRegistry create() {
        return this;
    }

    public <I extends Item> ItemEntry<I> item() {
        return new ItemEntry<>(itemRegister);
    }

    public <T extends Block> BlockEntry<T> block() {
        return new BlockEntry<>(blockRegister);
    }

    public FluidEntry fluid() {
        return new FluidEntry(fluidRegister, fluidTypeRegister, itemRegister, blockRegister, modId);
    }

    public <E extends BlockEntity> BlockEntityEntry<E> blockEntity() {
        return new BlockEntityEntry<>(blockEntityRegister);
    }

    public <E extends Entity> EntityEntry<E> entity() {
        return new EntityEntry<>(entityRegister, modId);
    }

    public <R extends Recipe<?>> RecipeEntry<R> recipe() {
        return new RecipeEntry<>(recipeTypeRegister, recipeSerializerRegister, modId);
    }

    public EffectEntry effect() {
        return new EffectEntry(effectRegister);
    }

    public <P extends ParticleOptions> ParticleEntry<P> particle() {
        return new ParticleEntry<>(particleTypeRegister);
    }

    public DamageTypeEntry damageType() {
        return new DamageTypeEntry(modId);
    }

    public SoundEntry sound() {
        return new SoundEntry(soundRegister, modId);
    }

    public <F extends FeatureConfiguration> FeatureEntry<F> feature() {
        return new FeatureEntry<>(featureRegister, modId);
    }

    public NetPackEntry netPack() {
        return new NetPackEntry(modId);
    }

    public CreativeTabEntry creativeTabEntry() {
        return new CreativeTabEntry(creativeTabRegister);
    }

}
