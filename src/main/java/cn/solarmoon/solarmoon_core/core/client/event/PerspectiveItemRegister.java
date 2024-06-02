package cn.solarmoon.solarmoon_core.core.client.event;

import cn.solarmoon.solarmoon_core.api.client.model.item.PerspectiveBakedModelRenderer;
import com.google.common.collect.Lists;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PerspectiveItemRegister {

    private static final List<Pair<ModelResourceLocation, ModelResourceLocation>> PERSPECTIVE_MODEL_LIST = Lists.newArrayList();

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.ITEM)) {

        }
    }

    @SubscribeEvent
    public static void onBakedModel(ModelEvent.BakingCompleted event) {
        Map<ResourceLocation, BakedModel> registry = event.getModelBakery().getBakedTopLevelModels();
        for (Pair<ModelResourceLocation, ModelResourceLocation> pair : PERSPECTIVE_MODEL_LIST) {
            PerspectiveBakedModelRenderer model = new PerspectiveBakedModelRenderer(registry.get(pair.getLeft()), registry.get(pair.getRight()));
            registry.put(pair.getLeft(), model);
        }
    }

    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterAdditional event) {
        PERSPECTIVE_MODEL_LIST.forEach((pair) -> event.register(pair.getRight()));
    }

    public static void addInHandModel(Item item) {
        ResourceLocation res = ForgeRegistries.ITEMS.getKey(item);
        if (res != null) {
            ModelResourceLocation rawName = new ModelResourceLocation(res, "inventory");
            ModelResourceLocation inHandName = new ModelResourceLocation(res.getNamespace(), res.getPath() + "_in_hand", "inventory");
            PERSPECTIVE_MODEL_LIST.add(Pair.of(rawName, inHandName));
        }
    }

}
