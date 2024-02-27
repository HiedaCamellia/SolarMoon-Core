package cn.solarmoon.solarmoon_core.common.block.crop;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

/**
 * 自动注册一个最大上限的age属性
 */
public interface INoLimitAgeBlock {

    IntegerProperty AGE = BlockStateProperties.AGE_25;

    int getMaxAge();

}
