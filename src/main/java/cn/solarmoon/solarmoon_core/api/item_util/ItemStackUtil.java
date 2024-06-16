package cn.solarmoon.solarmoon_core.api.item_util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class ItemStackUtil {

    /**
     * 是否一致并且能提供足够的数量给lessStack
     * @return 第一个输入物是否与第一个输入物物品类型一致，并且第一个输入物的量应当大于第二个输入物
     */
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
