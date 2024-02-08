package cn.solarmoon.solarmoon_core.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;

public class ItemHelper {

    private final Player player;

    public ItemHelper(Player player) {
        this.player = player;
    }

    /**
     * 获取双手上的特定物品，优先判断主手。
     * 需先检查player.isHolding，否则可能返回null
     */
    public <T extends Item> T getItemInHand(T item) {
        if (player.isHolding(item)) {
            if (player.getMainHandItem().is(item)) {
                return (T) player.getMainHandItem().getItem();
            } else if (player.getOffhandItem().is(item)) {
                return (T) player.getOffhandItem().getItem();
            }
        }
        return null;
    }

}
