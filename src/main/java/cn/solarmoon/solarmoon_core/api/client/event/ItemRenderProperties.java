package cn.solarmoon.solarmoon_core.api.client.event;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

/**
 * 用于给物品添加模型/贴图识别数据，可用于在json中使用override属性自定义某个属性值下的贴图显示路径
 */
public class ItemRenderProperties {

    private final String modId;

    public ItemRenderProperties(String modId) {
        this.modId = modId;
    }

    public void put(Item item, String id, ItemPropertyFunction itemPropertyFunction) {
        ItemProperties.register(item, new ResourceLocation(modId, id), itemPropertyFunction);
    }

}
