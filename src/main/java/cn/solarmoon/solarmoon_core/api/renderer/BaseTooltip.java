package cn.solarmoon.solarmoon_core.api.renderer;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

public abstract class BaseTooltip implements ClientTooltipComponent {

    protected final BaseTooltipComponent tooltipComponent;
    protected final ItemStack itemStack;

    public BaseTooltip(BaseTooltipComponent tooltipComponent) {
        this.tooltipComponent = tooltipComponent;
        itemStack = tooltipComponent.getItemStack();
    }

    /**
     * @return 高度，此是在当前提示的坐标下增加高度，从0开始较好
     */
    public abstract int getHeight();

    /**
     * @return 宽度
     * 实测这玩意是累增，这个相当于设置了个最小值
     */
    @Override
    public abstract int getWidth(Font font);

    @Override
    public abstract void renderText(Font font, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource buffer);

    @Override
    public abstract void renderImage(Font font, int x, int y, GuiGraphics guiGraphics);

}
