package cn.solarmoon.solarmoon_core.common.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;

public class SimpleFoodItem extends Item {

    public SimpleFoodItem(int nutrition, float saturation) {
        super(new Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(nutrition).saturationMod(saturation).build()));
    }

    public SimpleFoodItem(FoodProperties foodProperties) {
        super(new Properties()
                .food(foodProperties));
    }

}
