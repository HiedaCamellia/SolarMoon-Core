package cn.solarmoon.solarmoon_core.client.ItemRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 接入后可以快速填入想渲染的盔甲模型
 */
@OnlyIn(Dist.CLIENT)
public interface IItemArmorModelProvider {

    default EntityModelSet getEntityModelSet() {
        return Minecraft.getInstance().getEntityModels();
    }

    HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original);

}
