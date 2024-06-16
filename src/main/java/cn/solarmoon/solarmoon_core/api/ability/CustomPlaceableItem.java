package cn.solarmoon.solarmoon_core.api.ability;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 自定义已有物品的放置方块，一般用于难以直接覆盖的原版物品。需要自行对方块进行asItem设定
 */
public class CustomPlaceableItem {

    public static final HashMap<Item, Block> PLACE_MAP = new HashMap<>();

    public static void put(Item item, Block blockBound) {
        PLACE_MAP.put(item, blockBound);
    }

    public static Set<Map.Entry<Item, Block>> getBoundList() {
        return PLACE_MAP.entrySet();
    }

    public static HashMap<Item, Block> get() {
        return PLACE_MAP;
    }

}
