package cn.solarmoon.solarmoon_core.client.ItemRenderer;

import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

/**
 * 接入后实现自定义手持模型渲染
 */
@OnlyIn(Dist.CLIENT)
public interface IItemInventoryRendererProvider extends ItemLike {

    Supplier<BaseItemRenderer> getRendererFactory();

}
