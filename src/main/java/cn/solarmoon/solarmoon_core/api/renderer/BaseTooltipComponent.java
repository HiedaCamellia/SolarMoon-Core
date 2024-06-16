package cn.solarmoon.solarmoon_core.api.renderer;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public abstract class BaseTooltipComponent implements TooltipComponent {

    private final ItemStack itemStack;

    public BaseTooltipComponent(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

}
