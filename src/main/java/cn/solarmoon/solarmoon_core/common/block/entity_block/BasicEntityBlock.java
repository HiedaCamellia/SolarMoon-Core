package cn.solarmoon.solarmoon_core.common.block.entity_block;

import cn.solarmoon.solarmoon_core.common.block_entity.IContainerBlockEntity;
import cn.solarmoon.solarmoon_core.common.block_entity.ITankBlockEntity;
import cn.solarmoon.solarmoon_core.common.block_entity.iutor.IBlockEntityAnimateTicker;
import cn.solarmoon.solarmoon_core.common.block_entity.iutor.ITimeRecipeBlockEntity;
import cn.solarmoon.solarmoon_core.registry.SolarPacks;
import cn.solarmoon.solarmoon_core.util.ContainerUtil;
import cn.solarmoon.solarmoon_core.util.FluidUtil;
import cn.solarmoon.solarmoon_core.util.namespace.SolarNBTList;
import cn.solarmoon.solarmoon_core.util.namespace.SolarNETList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 有基本的含水、朝向、设置对应方块实体功能的实体方块<br/>
 * 以及一个ticker<br/>
 * 还是 万 物 本 源
 */
public abstract class BasicEntityBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected BasicEntityBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, false));
    }

    /**
     * 把方块快速拿到空手里
     */
    public boolean getThis(Player player, Level level, BlockPos pos, BlockState state, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        if(hand.equals(InteractionHand.MAIN_HAND) && heldItem.isEmpty() && player.isCrouching()) {
            ItemStack copy = getCloneItemStack(level, pos, state);
            level.removeBlock(pos, false);
            level.playSound(player, pos, SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1F, 1F);
            player.setItemInHand(hand, copy);
            return true;
        }
        return false;
    }

    /**
     * 必填项 - 碰撞箱
     */
    @Override
    public abstract VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context);

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, WATERLOGGED);
    }

    /**
     * @return 写入基本的放置状态
     */
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluid = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    /**
     * @return 设置转向、镜像
     */
    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    /**
     * 主要更新水流
     */
    @Override
    public @NotNull BlockState updateShape(BlockState stateIn, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        if (stateIn.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    /**
     * 创建一个ticker
     */
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, getBlockEntityType(), this::tick);
    }

    /**
     * @return 设置绑定的方块实体
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return getBlockEntityType().create(pos, state);
    }

    /**
     * 设置绑定的方块实体
     * 决定ticker所对应的实体类型（具体到注册类）
     */
    public abstract BlockEntityType<?> getBlockEntityType();

    /**
     * 设置实体的模型渲染类型
     */
    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    //FORGE START

    /**
     * 启用红石信号
     */
    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    /**
     * 默认进行容器的同步
     */
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        //防止放入物品未在客户端同步 而 造成的 假右键操作
        CompoundTag nbt = new CompoundTag();
        if (blockEntity instanceof IContainerBlockEntity containerTile) {
            nbt.put(SolarNBTList.INVENTORY, containerTile.getInventory().serializeNBT());
            SolarPacks.BASE_CLIENT_PACK.getSender().send(SolarNETList.SYNC_IC_BLOCK, pos, nbt);
        }
        if (blockEntity instanceof ITankBlockEntity tankTile) {
            nbt.put(SolarNBTList.FLUID, tankTile.getTank().writeToNBT(new CompoundTag()));
            SolarPacks.BASE_CLIENT_PACK.getSender().send(SolarNETList.SYNC_IT_BLOCK, pos, nbt);
        }
        //配方时间同步
        if (blockEntity instanceof ITimeRecipeBlockEntity<?> timeTile) {
            nbt.putInt(SolarNBTList.TIME, timeTile.getTime());
            SolarPacks.BASE_CLIENT_PACK.getSender().send(SolarNETList.SYNC_IRT_BLOCK, pos, nbt);
        }
        //实用接口动画ticker的实现
        if (blockEntity instanceof IBlockEntityAnimateTicker ticker) {
            int ticks = ticker.getTicks();
            if(ticks <= 100) ticks++;
            else ticks = 0;
            ticker.setTicks(ticks);
        }
    }

    /**
     * 使得放置物具有手上物品tank的读取
     * 以及item内的item的读取
     */
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @javax.annotation.Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof IContainerBlockEntity c) {
            c.setInventory(stack);
        }
        if (blockEntity instanceof ITankBlockEntity tankTile) {
            tankTile.setFluid(FluidUtil.getFluidStack(stack));
        }
    }

    /**
     * 使得复制物具有该实体的tank内容
     * 以及内容物
     */
    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof IContainerBlockEntity) {
            ContainerUtil.setInventory(stack, blockEntity);
        }
        if (blockEntity instanceof ITankBlockEntity) {
            FluidUtil.setTank(stack, blockEntity);
        }
        return stack;
    }

    /**
     * 让战利品表的同类掉落物存在tank信息和内容物信息
     */
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        List<ItemStack> drops = super.getDrops(state, builder);
        if(blockEntity != null) {
            for (ItemStack drop : drops) {
                if (drop.is(asItem())) {
                    if (blockEntity instanceof IContainerBlockEntity) {
                        ContainerUtil.setInventory(drop, blockEntity);
                    }
                    if (blockEntity instanceof ITankBlockEntity) {
                        FluidUtil.setTank(drop, blockEntity);
                    }
                }
            }
            return drops;
        }
        return drops;
    }

    /**
     * 设定红石信号逻辑<br/>
     * 按液体或物品比例输出信号<br/>
     * 两者皆有的情况下优先判断流体<br/>
     * 优先输出信号强的那一方
     */
    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        int containerSignal = 0;
        int tankSignal = 0;
        if (blockEntity instanceof IContainerBlockEntity) {
            containerSignal = (int) (ContainerUtil.getScale(blockEntity) * 15);
        }
        if (blockEntity instanceof ITankBlockEntity tankTile) {
            tankSignal = (int) (FluidUtil.getScale(tankTile.getTank()) * 15);
        }
        return Math.max(containerSignal, tankSignal);
    }

    //FORGE END

}
