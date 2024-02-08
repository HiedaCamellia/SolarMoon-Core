package cn.solarmoon.solarmoon_core.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class VecUtil {

    /**
     * 计算玩家面前一段距离的坐标位置
     */
    public static Vec3 getSpawnPosFrontPlayer(Player player, double distanceInFront) {
        Vec3 lookVec = player.getLookAngle();
        Vec3 inFrontVec = lookVec.scale(distanceInFront);
        return player.position().add(0, player.getEyeHeight(), 0).add(inFrontVec);
    }

}
