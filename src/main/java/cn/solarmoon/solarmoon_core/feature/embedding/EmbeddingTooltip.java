package cn.solarmoon.solarmoon_core.feature.embedding;

import cn.solarmoon.solarmoon_core.api.renderer.BaseTooltip;
import cn.solarmoon.solarmoon_core.api.renderer.BaseTooltipComponent;
import cn.solarmoon.solarmoon_core.api.renderer.ClipResList;
import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

import java.util.List;

public class EmbeddingTooltip extends BaseTooltip {

    private final EmbeddingData embeddingData;
    private final List<ItemStack> stacks;
    private final int count;

    private final float scale = 0.7f;

    public EmbeddingTooltip(BaseTooltipComponent tooltipComponent) {
        super(tooltipComponent);
        embeddingData = itemStack.getCapability(SolarCapabilities.ITEMSTACK_DATA).orElse(null).getEmbeddingData();
        stacks = embeddingData.getStacks();
        count = stacks.size();
    }

    @Override
    public int getHeight() {
        int row = count / 5; //count 为1时 + custom
        return canBeRendered() ? (int) (row * 19 * scale) + 5 : 0;
    }

    @Override
    public int getWidth(Font font) {
        int column = count < 5 ? count % 5 : 5; //1-4按数量来，大于等于5时就直接设为5
        return canBeRendered() ? (int) (column * 19 * scale) : 0;
    }

    @Override
    public void renderText(Font font, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource buffer) {

    }

    @Override
    public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
        if (canBeRendered()) {
            PoseStack poseStack = guiGraphics.pose();
            for (int i = 0; i < count; i++) {
                poseStack.pushPose();
                int column = i % 5;
                int row = i / 5;
                poseStack.scale(scale, scale, 1f);
                int pX = Math.round(x/scale) + 18 * column + 1;
                int pY = Math.round(y/scale) + 18 * row + 1;
                poseStack.translate(0, 0, 1000);
                guiGraphics.blit(ClipResList.EMBEDDING_SLOT, pX - 1, pY - 1, 0, 0, 18, 18);
                poseStack.translate(0, 0, 1000);
                guiGraphics.renderItem(stacks.get(i), pX, pY);
                int stackCount = stacks.get(i).getCount();
                if (stackCount > 1) {
                    String stackCountStr = String.valueOf(stackCount);
                    int stringWidth = font.width(stackCountStr);
                    poseStack.translate(0, 0, 1000);
                    guiGraphics.drawString(font, stackCountStr, pX + 18 - stringWidth, pY + 10, 0xFFFFFF);
                }
                poseStack.popPose();
            }
        }
    }

    public boolean canBeRendered() {
        return !embeddingData.getStacks().isEmpty();
    }

    public static class TooltipComponent extends BaseTooltipComponent {
        public TooltipComponent(ItemStack itemStack) {
            super(itemStack);
        }
    }

}
