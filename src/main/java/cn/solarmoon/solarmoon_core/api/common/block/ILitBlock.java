package cn.solarmoon.solarmoon_core.api.common.block;

import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

/**
 * 全自动添加lit属性
 */
public interface ILitBlock {

    BooleanProperty LIT = BlockStateProperties.LIT;

}
