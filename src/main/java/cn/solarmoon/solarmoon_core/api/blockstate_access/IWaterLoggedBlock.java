package cn.solarmoon.solarmoon_core.api.blockstate_access;

import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public interface IWaterLoggedBlock extends SimpleWaterloggedBlock {

    BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

}
