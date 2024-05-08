package cn.solarmoon.solarmoon_core.api.common.block.entity_block;

import cn.solarmoon.solarmoon_core.api.common.block.IBlockFunctionProvider;
import cn.solarmoon.solarmoon_core.api.common.block.IHorizontalFacingBlock;
import cn.solarmoon.solarmoon_core.api.common.block.IWaterLoggedBlock;
import cn.solarmoon.solarmoon_core.api.common.ability.BasicEntityBlockTicker;
import cn.solarmoon.solarmoon_core.api.common.block_entity.IContainerBlockEntity;
import cn.solarmoon.solarmoon_core.api.common.block_entity.ITankBlockEntity;
import cn.solarmoon.solarmoon_core.api.util.ContainerUtil;
import cn.solarmoon.solarmoon_core.api.util.FluidUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 有基本的含水、朝向、设置对应方块实体功能的实体方块<br/>
 * 以及一个ticker<br/>
 * 默认情况下会自动同步客户端各种容器的信息、设置红石信号逻辑、-中键物品、打落物品会存有给类容器信息、放置方块会读取stack的信息设置各类容器信息<br/>
 * 还是 万 物 本 源
 */
public abstract class BasicEntityBlock extends BaseEntityBlock implements IHorizontalFacingBlock, IWaterLoggedBlock, IBlockFunctionProvider {

    protected BasicEntityBlock(Properties properties) {
        super(properties);
    }

    /**
     * 必填项 - 碰撞箱
     */
    @Override
    public abstract VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context);

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
        if (!level.isClientSide) {
            CompoundTag nbt = new CompoundTag();
            //自定义同步
            BasicEntityBlockTicker.ALL.forEach(ticker -> ticker.getSynchronizer().ifPresent(c -> c.accept(blockEntity, nbt)));
        }
        //正常tick
        BasicEntityBlockTicker.ALL.forEach(ticker -> ticker.getTicker().ifPresent(c -> c.accept(blockEntity)));
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
