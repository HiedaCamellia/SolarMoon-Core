package cn.solarmoon.solarmoon_core.api.item_base;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class SimpleBlockItem extends BlockItem {

    public SimpleBlockItem(Block block) {
        super(block, new Properties());
    }

    public SimpleBlockItem(Block block, int stacksTo) {
        super(block, new Properties().stacksTo(stacksTo));
    }

}
