package cn.solarmoon.solarmoon_core.api.capability;

import cn.solarmoon.solarmoon_core.api.util.TagUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * 玩家计数器，多用于长按右键多次使用的情况。<br/>
 * 与blockPos绑定，
 */
public class CountingDevice implements INBTSerializable<CompoundTag> {

    private int timeTick;
    private int count;
    private BlockPos pos;

    public CountingDevice() {
        this.pos = new BlockPos(0, 0, 0);
    }

    /**
     * 直接设定数目
     */
    public void setCount(int count) {
        this.count = count;
        resetTime();
    }

    /**
     * 设定匹配指定坐标的数目<br/>
     * 如果不匹配坐标，就会重置count，并输入新坐标
     */
    public void setCount(int count, BlockPos pos) {
        if (pos.equals(this.pos)) {
            this.count = count;
        } else {
            this.pos = pos;
            this.count = 1; // 重置计数，但不是重置为0，因为pos只在调用此方法时才会做检查并修改，实际上已经右键了一次方块，这里相当于加上右键的第一次
        }
        resetTime();
    }

    public int getCount() {
        return count;
    }

    public void resetCount() {
        count = 0;
    }

    public void resetTime() {
        timeTick = 0;
    }

    /**
     * 当游戏中经过5tick时重置数量为0<br/>
     * 但在此期间setCount会重置时间计数
     */
    public void tick() {
        timeTick++;
        if (timeTick > 5) {
            timeTick = 0;
            count = 0;
        }
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("count", count);
        tag.putInt("TimeTick", timeTick);
        TagUtil.putPos(tag, pos);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        count = nbt.getInt("count");
        timeTick = nbt.getInt("TimeTick");
        pos = TagUtil.getBlockPos(nbt);
    }

}
