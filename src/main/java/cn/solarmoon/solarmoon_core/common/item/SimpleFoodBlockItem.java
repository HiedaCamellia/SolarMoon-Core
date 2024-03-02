package cn.solarmoon.solarmoon_core.common.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;

public class SimpleFoodBlockItem extends BlockItem {

    public SimpleFoodBlockItem(Block block, FoodProperties foodProperties) {
        super(block, new Properties().food(foodProperties));
    }

    public SimpleFoodBlockItem(Block block, int nutrition, float saturation) {
        super(block, new Properties().food(new FoodProperties.Builder()
                .nutrition(nutrition).saturationMod(saturation).build()));
    }

}
