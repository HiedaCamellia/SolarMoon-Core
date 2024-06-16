package cn.solarmoon.solarmoon_core.mixin;

import cn.solarmoon.solarmoon_core.api.phys.OrientedBox;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.debug.DebugRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DebugRenderer.class)
public class DebugRendererMixin {
    @Inject(method = "render", at = @At("TAIL"))
    public void renderColliderDebug(PoseStack poseStack, MultiBufferSource.BufferSource buffer, double light, double overlay, double d, CallbackInfo ci) {
        Minecraft client = Minecraft.getInstance();
        if (!client.getEntityRenderDispatcher().shouldRenderHitBoxes()) {
            return;
        }
        LocalPlayer player = client.player;
        if (player == null) {
            return;
        }
        OrientedBox obb = new OrientedBox(player, 10, 10, 10);
        List<OrientedBox> collidingObbs = List.of(new OrientedBox(player, 1, 1, 1));
        drawOutline(poseStack, buffer, obb, collidingObbs, true);
    }

    private void drawOutline(PoseStack poseStack, MultiBufferSource.BufferSource buffer, OrientedBox obb, List<OrientedBox> otherObbs, boolean collides) {
        RenderSystem.enableDepthTest();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableBlend();
        RenderSystem.lineWidth(1.0f);
        Matrix4f matrix4f = poseStack.last().pose();
        VertexConsumer consumer = buffer.getBuffer(RenderType.leash());

        if (collides) {
            //System.out.println("Drawing collider +");
            outlineOBB(matrix4f, obb, consumer,
                    1, 0, 0,
                    1, 0, 0,0.5F);
        } else {
            //System.out.println("Drawing collider -");
            outlineOBB(matrix4f, obb, consumer,
                    0, 1, 0,
                    1, 1, 0,0.5F);
        }
        look(matrix4f, obb, consumer, 0.5F);

        for(OrientedBox otherObb: otherObbs){
            outlineOBB(matrix4f, otherObb, consumer,
                    1, 0, 0,
                    1, 0, 0,0.5F);
        }

        RenderSystem.lineWidth(1.0f);
        RenderSystem.enableBlend();
    }

    private void outlineOBB(Matrix4f matrix4f, OrientedBox box, VertexConsumer buffer,
                            float red1, float green1, float blue1,
                            float red2, float green2, float blue2,
                            float alpha) {
        buffer.vertex(matrix4f, (float) box.vertex1.x, (float) box.vertex1.y, (float) box.vertex1.z).color(0, 0, 0, 0).uv2(1).endVertex();

        buffer.vertex(matrix4f, (float) box.vertex1.x, (float) box.vertex1.y, (float) box.vertex1.z).color(red1, green1, blue1, alpha).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex2.x, (float) box.vertex2.y, (float) box.vertex2.z).color(red1, green1, blue1, alpha).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex3.x, (float) box.vertex3.y, (float) box.vertex3.z).color(red1, green1, blue1, alpha).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex4.x, (float) box.vertex4.y, (float) box.vertex4.z).color(red1, green1, blue1, alpha).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex1.x, (float) box.vertex1.y, (float) box.vertex1.z).color(red1, green1, blue1, alpha).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex5.x, (float) box.vertex5.y, (float) box.vertex5.z).color(red2, green2, blue2, alpha).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex6.x, (float) box.vertex6.y, (float) box.vertex6.z).color(red2, green2, blue2, alpha).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex2.x, (float) box.vertex2.y, (float) box.vertex2.z).color(red1, green1, blue1, alpha).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex6.x, (float) box.vertex6.y, (float) box.vertex6.z).color(red2, green2, blue2, alpha).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex7.x, (float) box.vertex7.y, (float) box.vertex7.z).color(red2, green2, blue2, alpha).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex3.x, (float) box.vertex3.y, (float) box.vertex3.z).color(red1, green1, blue1, alpha).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex7.x, (float) box.vertex7.y, (float) box.vertex7.z).color(red2, green2, blue2, alpha).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex8.x, (float) box.vertex8.y, (float) box.vertex8.z).color(red2, green2, blue2, alpha).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex4.x, (float) box.vertex4.y, (float) box.vertex4.z).color(red1, green1, blue1, alpha).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex8.x, (float) box.vertex8.y, (float) box.vertex8.z).color(red2, green2, blue2, alpha).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.vertex5.x, (float) box.vertex5.y, (float) box.vertex5.z).color(red2, green2, blue2, alpha).uv2(1).endVertex();

        buffer.vertex(matrix4f, (float) box.vertex5.x, (float) box.vertex5.y, (float) box.vertex5.z).color(0, 0, 0, 0).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.center.x, (float) box.center.y, (float) box.center.z).color(0, 0, 0, 0).uv2(1).endVertex();
    }

    private void look(Matrix4f matrix4f, OrientedBox box, VertexConsumer buffer, float alpha) {
        buffer.vertex(matrix4f, (float) box.center.x, (float) box.center.y, (float) box.center.z).color(0, 0, 0, alpha).uv2(1).endVertex();

        buffer.vertex(matrix4f, (float) box.center.x, (float) box.center.y, (float) box.center.z).color(1, 0, 0, alpha).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.axisZ.x, (float) box.axisZ.y, (float) box.axisZ.z).color(1, 0, 0, alpha).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.center.x, (float) box.center.y, (float) box.center.z).color(1, 0, 0, alpha).uv2(1).endVertex();

        buffer.vertex(matrix4f, (float) box.center.x, (float) box.center.y, (float) box.center.z).color(0, 1, 0, alpha).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.axisY.x, (float) box.axisY.y, (float) box.axisY.z).color(0, 1, 0, alpha).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.center.x, (float) box.center.y, (float) box.center.z).color(0, 1, 0, alpha).uv2(1).endVertex();

        buffer.vertex(matrix4f, (float) box.center.x, (float) box.center.y, (float) box.center.z).color(0, 0, 1, alpha).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.axisX.x, (float) box.axisX.y, (float) box.axisX.z).color(0, 0, 1, alpha).uv2(1).endVertex();
        buffer.vertex(matrix4f, (float) box.center.x, (float) box.center.y, (float) box.center.z).color(0, 0, 1, alpha).uv2(1).endVertex();

        buffer.vertex(matrix4f, (float) box.center.x, (float) box.center.y, (float) box.center.z).color(0, 0, 0, alpha).uv2(1).endVertex();
    }
}
