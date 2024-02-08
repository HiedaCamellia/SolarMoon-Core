package cn.solarmoon.solarmoon_core.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class CountingDevice {

    /**
     * 玩家计数器
     * 可用于计数点击方块次数
     */
    public static CompoundTag player(Player player, BlockPos pos, Level level) {
        CompoundTag playerTag = player.getPersistentData();
        int lastCount = playerTag.getInt("PressCount");
        playerTag.putInt("PressCount", lastCount + 1);
        int x = playerTag.getInt("PressX");
        int y = playerTag.getInt("PressY");
        int z = playerTag.getInt("PressZ");
        BlockPos pressPos = new BlockPos(x, y, z);
        if(level.getGameTime() - playerTag.getLong("PressTime") > 5
                || !pressPos.equals(pos)) {
            playerTag.putInt("PressCount", 0);
        }
        playerTag.putInt("PressX", pos.getX());
        playerTag.putInt("PressY", pos.getY());
        playerTag.putInt("PressZ", pos.getZ());
        playerTag.putLong("PressTime", level.getGameTime());
        return playerTag;
    }

    public static void resetCount(CompoundTag tag) {
        tag.putInt("PressCount", 0);
    }

    public static void resetCount(CompoundTag tag, int i) {
        tag.putInt("PressCount", i);
    }

    public static int getCount(CompoundTag tag) {
        return tag.getInt("PressCount");
    }

}
