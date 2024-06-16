package cn.solarmoon.solarmoon_core.api.item_base;

import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;

public class SimpleSeedItem extends ItemNameBlockItem {

    public SimpleSeedItem(Block block) {
        super(block, new Properties());
    }

    public SimpleSeedItem(Block block, Properties properties) {
        super(block, properties);
    }

}
