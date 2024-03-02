package cn.solarmoon.solarmoon_core.registry.ability;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 自定义已有物品的放置方块，一般用于难以直接覆盖的原版物品。需要自行对方块进行asItem设定
 */
public class CustomPlaceableItem {

    private static final HashMap<Item, Block> placeMap = new HashMap<>();

    public static void put(Item item, Block blockBound) {
        placeMap.put(item, blockBound);
    }

    public static Set<Map.Entry<Item, Block>> getBoundList() {
        return placeMap.entrySet();
    }

    public static HashMap<Item, Block> get() {
        return placeMap;
    }

}
