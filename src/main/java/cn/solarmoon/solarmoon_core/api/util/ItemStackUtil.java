package cn.solarmoon.solarmoon_core.api.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class ItemStackUtil {

    public static boolean isSameAndSufficient(ItemStack stackMore, ItemStack stackLess, boolean compareNBT) {
        boolean nbtMatch = !compareNBT || stackLess.areShareTagsEqual(stackMore);
        return stackLess.is(stackMore.getItem())
                && stackMore.getCount() >= stackLess.getCount()
                && nbtMatch;
    }

    /**
     * 获取双手上的特定物品，优先判断主手。
     */
    @Nullable
    public static <T extends Item> ItemStack getItemInHand(Player player, T item) {
        if (player.isHolding(item)) {
            if (player.getMainHandItem().is(item)) {
                return player.getMainHandItem();
            } else if (player.getOffhandItem().is(item)) {
                return player.getOffhandItem();
            }
        }
        return null;
    }

}
