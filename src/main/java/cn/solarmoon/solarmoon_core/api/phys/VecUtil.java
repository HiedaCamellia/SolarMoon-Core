package cn.solarmoon.solarmoon_core.api.phys;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class VecUtil {

    /**
     * 计算生物面前一段距离的坐标位置，高度默认和玩家视线齐平
     */
    public static Vec3 getSpawnPosFrontEntity(LivingEntity living, double distanceInFront) {
        Vec3 lookVec = living.getLookAngle();
        Vec3 inFrontVec = lookVec.scale(distanceInFront);
        return living.position().add(0.0, living.getEyeHeight(), 0.0).add(inFrontVec);
    }

    /**
     * 计算生物面前一段距离的坐标位置，且能调整高度
     */
    public static Vec3 getSpawnPosFrontEntity(LivingEntity living, double distanceInFront, double yOffset) {
        Vec3 lookVec = living.getLookAngle();
        Vec3 inFrontVec = lookVec.scale(distanceInFront);
        return living.position().add(0.0, (double)living.getEyeHeight() + yOffset, 0.0).add(inFrontVec);
    }

    /**
     * 把输入的vec以输入的center为中心按照direction的角度进行旋转（一般用于需要在方块上创建相对固定坐标的情况）
     */
    public static Vec3 rotateVec(Vec3 point, Vec3 center, Direction direction) {
        // 转换为相对坐标
        Vec3 relative = point.subtract(center);
        // 获取旋转角度
        double angle = Math.toRadians(direction.toYRot());
        // 计算旋转矩阵
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        // 旋转相对坐标
        double x = relative.x * cos - relative.z * sin;
        double z = relative.x * sin + relative.z * cos;
        // 转换回绝对坐标
        return new Vec3(x, relative.y, z).add(center);
    }

    /**
     * 判断某个点是否在一个矩形范围内
     * @param point 落点
     * @param rangePoint1 矩形对角坐标1
     * @param rangePoint2 矩形对角坐标2
     */
    public static boolean inRange(Vec3 point, Vec3 rangePoint1, Vec3 rangePoint2) {
        double x1 = Math.min(rangePoint1.x, rangePoint2.x);
        double x2 = Math.max(rangePoint1.x, rangePoint2.x);
        double y1 = Math.min(rangePoint1.y, rangePoint2.y);
        double y2 = Math.max(rangePoint1.y, rangePoint2.y);
        double z1 = Math.min(rangePoint1.z, rangePoint2.z);
        double z2 = Math.max(rangePoint1.z, rangePoint2.z);
        return x1 <= point.x && point.x <= x2 && y1 <= point.y && point.y <= y2 && z1 <= point.z && point.z <= z2; //立体矩形范围判别
    }

    /**
     * @param vec 落点
     * @param pos 比较的方块坐标
     * @return 判断vec3落点是否在这个pos所在1x1x1大小的方块的内部
     */
    public static boolean isInside(Vec3 vec, BlockPos pos) {
        double x = vec.x - pos.getX();
        double y = vec.y - pos.getY();
        double z = vec.z - pos.getZ();
        return x > 0 && x < 1 && y > 0 && y < 1 && z > 0 && z < 1;
    }

    /**
     * @param vec 落点
     * @param pos 比较的方块坐标
     * @return 判断vec3落点是否在这个pos所在指定顶点大小的方块的内部
     */
    public static boolean isInside(Vec3 vec, BlockPos pos, double xMin, double yMin, double zMin, double xMax, double yMax, double zMax, boolean border) {
        double x = vec.x - pos.getX();
        double y = vec.y - pos.getY();
        double z = vec.z - pos.getZ();
        if (border) return x >= xMin && x <= xMax && y >= yMin && y <= yMax && z >= zMin && z <= zMax;
        return x > xMin && x < xMax && y > yMin && y < yMax && z > zMin && z < zMax;
    }

}
