package cn.solarmoon.solarmoon_core.client.blockEntityRenderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class BaseBlockEntityRenderer<E extends BlockEntity> implements BlockEntityRenderer<E> {

    protected ItemRenderer itemRenderer;

     public BaseBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
     }

    @Override
    public abstract void render(E blockEntity, float tickDelta, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay);

}
