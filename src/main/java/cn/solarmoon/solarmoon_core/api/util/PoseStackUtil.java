package cn.solarmoon.solarmoon_core.api.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.Direction;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;

public class PoseStackUtil {

    /**
     * 根据方块朝向进行旋转
     */
    public static void rotateByDirection(Direction direction, PoseStack poseStack) {
        Quaternionf angle = direction.getRotation();
        Quaternionf rotationZ = new Quaternionf(new AxisAngle4f((float)Math.PI, 0.0f, 0.0f, 1.0f));
        Quaternionf result = angle.mul(rotationZ);
        poseStack.mulPose(result);
    }

}
