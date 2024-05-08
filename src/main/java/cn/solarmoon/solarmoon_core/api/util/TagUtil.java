package cn.solarmoon.solarmoon_core.api.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Block;

public class TagUtil {

    public static void putPos(CompoundTag tagToPutIn, BlockPos pos) {
        CompoundTag posTag = new CompoundTag();
        posTag.putInt("x", pos.getX());
        posTag.putInt("y", pos.getY());
        posTag.putInt("z", pos.getZ());
        tagToPutIn.put("BlockPos", posTag);
    }

    public static BlockPos getBlockPos(CompoundTag tagToGet) {
        CompoundTag posTag = tagToGet.getCompound("BlockPos");
        return new BlockPos(posTag.getInt("x"), posTag.getInt("y"), posTag.getInt("z"));
    }

}
