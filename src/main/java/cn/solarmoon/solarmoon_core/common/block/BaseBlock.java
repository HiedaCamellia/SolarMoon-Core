package cn.solarmoon.solarmoon_core.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 有基本的朝向功能的方块（四面）<br/>
 * 并且能快速添加自定义属性
 */
public abstract class BaseBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    @SuppressWarnings("unchecked")
    public BaseBlock(Properties properties) {
        super(properties);
        //这里的设置会被覆盖，要改全改
        registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    /**
     * 用到createBlockState里的，用于设置初始化阶段要创建的属性
     */
    public List<Property<?>> getProperties() {
        List<Property<?>> properties = new ArrayList<>();
        properties.add(FACING);
        return properties;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        for (var entry : getProperties()) {
            builder.add(entry);
        }
    }

    /**
     * @return 写入基本的放置朝向状态，此处如果是方块本身固有的属性，就无需在这里添加，反之类似含水等需要参照周围状况的需要在此添加
     */
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context)
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    /**
     * @return 设置转向
     */
    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    /**
     * 设置镜像
     */
    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    /**
     * @return 设置碰撞箱
     */
    @Override
    public abstract VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context);

    /**
     * 能发出红石信号
     */
    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

}
