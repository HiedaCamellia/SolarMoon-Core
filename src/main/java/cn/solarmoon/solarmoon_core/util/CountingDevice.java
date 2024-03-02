package cn.solarmoon.solarmoon_core.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

/**
 * 玩家计数器<br/>
 * 可用于计数点击方块次数（未来会用capability代替）
 */
@Deprecated
public class CountingDevice {

    private final Player player;
    private final Level level;

    private CompoundTag playerTag;

    public CountingDevice(Player player, BlockPos clickedPos) {
        this.player = player;
        level = player.level();
        setTag(clickedPos);
    }

    public void setTag(BlockPos clickedPos) {
        playerTag = player.getPersistentData();
        int lastCount = playerTag.getInt("PressCount");
        playerTag.putInt("PressCount", lastCount + 1);
        int x = playerTag.getInt("PressX");
        int y = playerTag.getInt("PressY");
        int z = playerTag.getInt("PressZ");
        BlockPos pressPos = new BlockPos(x, y, z);
        if(level.getGameTime() - playerTag.getLong("PressTime") > 5
                || !pressPos.equals(clickedPos)) {
            playerTag.putInt("PressCount", 0);
        }
        playerTag.putInt("PressX", clickedPos.getX());
        playerTag.putInt("PressY", clickedPos.getY());
        playerTag.putInt("PressZ", clickedPos.getZ());
        playerTag.putLong("PressTime", level.getGameTime());
    }

    public CompoundTag getPlayerTag() {
        return playerTag;
    }

    public void resetCount() {
        playerTag.putInt("PressCount", 0);
    }

    public void setCount(int i) {
        playerTag.putInt("PressCount", i);
    }

    public int getCount() {
        return playerTag.getInt("PressCount");
    }

}
