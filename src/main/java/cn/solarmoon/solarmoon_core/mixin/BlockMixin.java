package cn.solarmoon.solarmoon_core.mixin;

import cn.solarmoon.solarmoon_core.common.block.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.common.block.IStackBlock;
import cn.solarmoon.solarmoon_core.common.block.IWaterLoggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin extends BlockBehaviour {

    @Shadow public abstract BlockState defaultBlockState();

    private Block block = (Block)(Object)this;

    public BlockMixin(Properties p_60452_) {
        super(p_60452_);
    }

    @Inject(method = "registerDefaultState", at = @At("HEAD"))
    public void registerDefaultState(BlockState state, CallbackInfo ci) {
        if (block instanceof IHorizontalFacingBlock facingBlock) {
            state.setValue(facingBlock.FACING, Direction.NORTH);
        }
        if (block instanceof IWaterLoggedBlock waterBlock) {
            state.setValue(waterBlock.WATERLOGGED, false);
        }
        if (block instanceof IStackBlock stackBlock) {
            state.setValue(stackBlock.STACK, 1);
        }
    }

    @Inject(method = "createBlockStateDefinition", at = @At("HEAD"))
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        if (block instanceof IHorizontalFacingBlock facingBlock) {
            builder.add(facingBlock.FACING);
        }
        if (block instanceof IWaterLoggedBlock waterBlock) {
            builder.add(waterBlock.WATERLOGGED);
        }
        if (block instanceof IStackBlock stackBlock) {
            builder.add(stackBlock.STACK);
        }
    }

    @Inject(method = "getStateForPlacement", at = @At("HEAD"), cancellable = true)
    public void getStateForPlacement(BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir) {
        BlockState state = this.defaultBlockState();
        if (block instanceof IHorizontalFacingBlock facingBlock) {
            state.setValue(facingBlock.FACING, context.getHorizontalDirection().getOpposite());
        }
        if (block instanceof IWaterLoggedBlock waterBlock) {
            FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
            state.setValue(waterBlock.WATERLOGGED, fluid.getType() == Fluids.WATER);
        }
        cir.setReturnValue(state);
    }

    /**
     * @return 更新水流
     */
    @Override
    public BlockState updateShape(BlockState stateIn, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        if (block instanceof IWaterLoggedBlock waterBlock) {
            if (stateIn.getValue(waterBlock.WATERLOGGED)) {
                level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
            }
        }
        return super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if (block instanceof IWaterLoggedBlock waterBlock) {
            return state.getValue(waterBlock.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
        }
        return super.getFluidState(state);
    }

    /**
     * 设置转向
     */
    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        if (block instanceof IHorizontalFacingBlock facingBlock) {
            return super.rotate(state, rot).setValue(facingBlock.FACING, rot.rotate(state.getValue(facingBlock.FACING)));
        }
        return super.rotate(state, rot);
    }

    /**
     * 设置镜像
     */
    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        if (block instanceof IHorizontalFacingBlock facingBlock) {
            return super.mirror(state, mirror).rotate(mirror.getRotation(state.getValue(facingBlock.FACING)));
        }
        return super.mirror(state, mirror);
    }

}
