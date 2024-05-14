package cn.solarmoon.solarmoon_core.api.util.phys;

import cn.solarmoon.solarmoon_core.api.util.VecUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;

/**
 * Credit: <a href="https://github.com/ZsoltMolnarrr/BetterCombat/tree/1.20.4">...</a>
 */
public class OrientedBox {

    // Y ^       8   +-------+   7     axisY   axisZ
    //   |          /|      /|             | /
    //   |     4   +-------+ | 3           |/
    //   |  Z      | |     | |             +-- axisX
    //   |   /   5 | +-----|-+  6       Center
    //   |  /      |/      |/
    //   | /   1   +-------+   2
    //   |/
    //   +--------------------> X

    public Vec3 center;

    // Extent defines the half size in all directions
    public Vec3 extent;

    // Orthogonal basis vectors define orientation
    public Vec3 axisX;
    public Vec3 axisY;
    public Vec3 axisZ;

    // DERIVED PROPERTIES
    public Vec3 scaledAxisX;
    public Vec3 scaledAxisY;
    public Vec3 scaledAxisZ;
    public Matrix3f rotation = new Matrix3f();
    public Vec3 vertex1;
    public Vec3 vertex2;
    public Vec3 vertex3;
    public Vec3 vertex4;
    public Vec3 vertex5;
    public Vec3 vertex6;
    public Vec3 vertex7;
    public Vec3 vertex8;
    public Vec3[] vertices;

    // 1. CONSTRUCT

    public OrientedBox(Vec3 center, double width, double height, double depth, float yaw, float pitch) {
        this.center = center;
        this.extent = new Vec3(width/2.0f, height/2.0f, depth/2.0f);
        this.axisZ = Vec3.directionFromRotation(yaw, pitch).normalize();
        this.axisY = Vec3.directionFromRotation(yaw + 90, pitch).normalize();
        this.axisX = axisZ.cross(axisY);
        updateVertex();
    }

    public OrientedBox(Vec3 center, Vec3 size, float yaw, float pitch) {
        this(center,size.x, size.y, size.z, yaw, pitch);
    }

    public OrientedBox(LivingEntity entity, Vec3 size) {
        this(VecUtil.getSpawnPosFrontEntity(entity, size.z / 2), size, entity.getXRot(), entity.getYRot());
    }

    public OrientedBox(LivingEntity entity, float width, float height, float depth) {
        this(VecUtil.getSpawnPosFrontEntity(entity, depth / 2), width, height, depth, entity.getXRot(), entity.getYRot());
    }

    public OrientedBox(LivingEntity entity, double distanceInFrontPlayer, float width, float height, float depth) {
        this(VecUtil.getSpawnPosFrontEntity(entity, distanceInFrontPlayer), width, height, depth, entity.getXRot(), entity.getYRot());
    }

    public OrientedBox(AABB box) {
        this.center = new Vec3((float) ((box.maxX + box.minX) / 2.0), (float) ((box.maxY + box.minY) / 2.0), (float) ((box.maxZ + box.minZ) / 2.0));
        this.extent = new Vec3((float) (Math.abs(box.maxX - box.minX) / 2.0), (float) (Math.abs(box.maxY - box.minY) / 2.0), (float) (Math.abs(box.maxZ - box.minZ) / 2.0));
        this.axisX = new Vec3(1, 0, 0);
        this.axisY = new Vec3(0, 1, 0);
        this.axisZ = new Vec3(0, 0, 1);
        updateVertex();
    }

    public OrientedBox(OrientedBox obb) {
        this.center = obb.center;
        this.extent = obb.extent;
        this.axisX = obb.axisX;
        this.axisY = obb.axisY;
        this.axisZ = obb.axisZ;
        updateVertex();
    }

    public OrientedBox copy() {
        return new OrientedBox(this);
    }

    // 2. CONFIGURE

    public OrientedBox offsetAlongAxisX(double offset) {
        this.center = this.center.add(axisX.scale(offset));
        return this;
    }

    public OrientedBox offsetAlongAxisY(double offset) {
        this.center = this.center.add(axisY.scale(offset));
        return this;
    }

    public OrientedBox offsetAlongAxisZ(double offset) {
        this.center = this.center.add(axisZ.scale(offset));
        return this;
    }

    public OrientedBox offset(Vec3 offset) {
        this.center = this.center.add(offset);
        return this;
    }

    public OrientedBox scale(double scale) {
        this.extent = extent.scale(scale);
        return this;
    }

    // 3. UPDATE

    public OrientedBox updateVertex() {
        rotation.set(0,0, (float) axisX.x);
        rotation.set(0,1, (float) axisX.y);
        rotation.set(0,2, (float) axisX.z);
        rotation.set(1,0, (float) axisY.x);
        rotation.set(1,1, (float) axisY.y);
        rotation.set(1,2, (float) axisY.z);
        rotation.set(2,0, (float) axisZ.x);
        rotation.set(2,1, (float) axisZ.y);
        rotation.set(2,2, (float) axisZ.z);

        scaledAxisX = axisX.scale(extent.x);
        scaledAxisY = axisY.scale(extent.y);
        scaledAxisZ = axisZ.scale(extent.z);

        vertex1 = center.subtract(scaledAxisZ).subtract(scaledAxisX).subtract(scaledAxisY);
        vertex2 = center.subtract(scaledAxisZ).add(scaledAxisX).subtract(scaledAxisY);
        vertex3 = center.subtract(scaledAxisZ).add(scaledAxisX).add(scaledAxisY);
        vertex4 = center.subtract(scaledAxisZ).subtract(scaledAxisX).add(scaledAxisY);
        vertex5 = center.add(scaledAxisZ).subtract(scaledAxisX).subtract(scaledAxisY);
        vertex6 = center.add(scaledAxisZ).add(scaledAxisX).subtract(scaledAxisY);
        vertex7 = center.add(scaledAxisZ).add(scaledAxisX).add(scaledAxisY);
        vertex8 = center.add(scaledAxisZ).subtract(scaledAxisX).add(scaledAxisY);

        vertices = new Vec3[]{
                vertex1,
                vertex2,
                vertex3,
                vertex4,
                vertex5,
                vertex6,
                vertex7,
                vertex8
        };

        return this;
    }

    // 4. CHECK INTERSECTIONS

    public boolean intersects(AABB boundingBox) {
        var otherOBB = new OrientedBox(boundingBox).updateVertex();
        return Intersects(this, otherOBB);
    }

    public boolean intersects(OrientedBox otherOBB) {
        return Intersects(this, otherOBB);
    }

    /**
     * Calculates if there is intersection between given OBBs.
     * Separating Axes Theorem implementation.
     */
    public static boolean Intersects(OrientedBox a, OrientedBox b)  {
        if (Separated(a.vertices, b.vertices, a.scaledAxisX))
            return false;
        if (Separated(a.vertices, b.vertices, a.scaledAxisY))
            return false;
        if (Separated(a.vertices, b.vertices, a.scaledAxisZ))
            return false;

        if (Separated(a.vertices, b.vertices, b.scaledAxisX))
            return false;
        if (Separated(a.vertices, b.vertices, b.scaledAxisY))
            return false;
        if (Separated(a.vertices, b.vertices, b.scaledAxisZ))
            return false;

        if (Separated(a.vertices, b.vertices, a.scaledAxisX.cross(b.scaledAxisX)))
            return false;
        if (Separated(a.vertices, b.vertices, a.scaledAxisX.cross(b.scaledAxisY)))
            return false;
        if (Separated(a.vertices, b.vertices, a.scaledAxisX.cross(b.scaledAxisZ)))
            return false;

        if (Separated(a.vertices, b.vertices, a.scaledAxisY.cross(b.scaledAxisX)))
            return false;
        if (Separated(a.vertices, b.vertices, a.scaledAxisY.cross(b.scaledAxisY)))
            return false;
        if (Separated(a.vertices, b.vertices, a.scaledAxisY.cross(b.scaledAxisZ)))
            return false;

        if (Separated(a.vertices, b.vertices, a.scaledAxisZ.cross(b.scaledAxisX)))
            return false;
        if (Separated(a.vertices, b.vertices, a.scaledAxisZ.cross(b.scaledAxisY)))
            return false;
        if (Separated(a.vertices, b.vertices, a.scaledAxisZ.cross(b.scaledAxisZ)))
            return false;

        return true;
    }

    private static boolean Separated(Vec3[] vertsA, Vec3[] vertsB, Vec3 axis)  {
        // Handles the crossProduct product = {0,0,0} case
        if (axis.equals(Vec3.ZERO))
            return false;

        var aMin = Double.POSITIVE_INFINITY;
        var aMax = Double.NEGATIVE_INFINITY;
        var bMin = Double.POSITIVE_INFINITY;
        var bMax = Double.NEGATIVE_INFINITY;

        // Define two intervals, a and b. Calculate their min and max values
        for (var i = 0; i < 8; i++)
        {
            var aDist = vertsA[i].dot(axis);
            aMin = Math.min(aDist, aMin);
            aMax = Math.max(aDist, aMax);
            var bDist = vertsB[i].dot(axis);
            bMin = Math.min(bDist, bMin);
            bMax = Math.max(bDist, bMax);
        }

        // One-dimensional intersection test between a and b
        var longSpan = Math.max(aMax, bMax) - Math.min(aMin, bMin);
        var sumSpan = aMax - aMin + bMax - bMin;
        return longSpan >= sumSpan; // > to treat touching as intersection
    }
}
