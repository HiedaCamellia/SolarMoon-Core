package cn.solarmoon.solarmoon_core.registry.object;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.function.Supplier;

public class LayerEntry {

    private final String modId;

    private String id;
    private Supplier<LayerDefinition> layerSupplier;
    private String layerId;
    private ModelLayerLocation layer;

    public LayerEntry(String modId) {
        this.modId = modId;
        layerId = "main";
    }

    public LayerEntry id(String id) {
        this.id = id;
        return this;
    }

    public LayerEntry bound(Supplier<LayerDefinition> layerSupplier) {
        this.layerSupplier = layerSupplier;
        return this;
    }

    public LayerEntry layerId(String layerId) {
        this.layerId = layerId;
        return this;
    }

    public LayerEntry build() {
        this.layer = new ModelLayerLocation(new ResourceLocation(modId, id), layerId);
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        if (FMLEnvironment.dist.isClient()) {
            bus.addListener(this::initLayers);
        }
        return this;
    }

    public ModelLayerLocation get() {
        return layer;
    }

    @SubscribeEvent
    public void initLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(layer, layerSupplier);
    }

}
