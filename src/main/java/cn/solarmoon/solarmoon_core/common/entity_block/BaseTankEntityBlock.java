package cn.solarmoon.solarmoon_core.common.entity_block;

import cn.solarmoon.solarmoon_core.common.entity_block.entity.BaseTankBlockEntity;
import cn.solarmoon.solarmoon_core.registry.Packs;
import cn.solarmoon.solarmoon_core.util.FluidUtil;
import cn.solarmoon.solarmoon_core.util.namespace.SolarNBTList;
import cn.solarmoon.solarmoon_core.util.namespace.SolarNETList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public abstract class BaseTankEntityBlock extends BasicEntityBlock {

    public BaseTankEntityBlock(Properties properties) {
        super(properties);
    }

    /**
     * 强制继承类实现use，因为容器一般需要特殊功能
     */
    @Override
    public abstract InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult);

    public boolean putFluid(BaseTankBlockEntity tankEntity, Player player, InteractionHand hand, boolean playSound) {
        ItemStack heldItem = player.getItemInHand(hand);
        FluidTank tank = tankEntity.tank;
        FluidActionResult result = net.minecraftforge.fluids.FluidUtil.tryEmptyContainer(heldItem, tank, Integer.MAX_VALUE, playSound ? player : null, true);
        if (result.isSuccess()) {
            if (!player.isCreative()) player.setItemInHand(hand, result.getResult());
            tankEntity.setChanged();
            return true;
        }
        return false;
    }

    public boolean takeFluid(BaseTankBlockEntity tankEntity, Player player, InteractionHand hand, boolean playSound) {
        ItemStack heldItem = player.getItemInHand(hand);
        FluidTank tank = tankEntity.tank;
        FluidActionResult result = net.minecraftforge.fluids.FluidUtil.tryFillContainer(heldItem, tank, Integer.MAX_VALUE, playSound ? player : null, true);
        if (result.isSuccess()) {
            if (!player.isCreative()) player.setItemInHand(hand, result.getResult());
            tankEntity.setChanged();
            return true;
        }
        return false;
    }

    /**
     * 默认进行流体的同步
     */
    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        //防止放入液体时液体值和物品未在客户端同步 而 造成的 假右键操作
        if (blockEntity instanceof BaseTankBlockEntity t) {
            CompoundTag tag = new CompoundTag();
            tag.put(SolarNBTList.FLUID, t.tank.writeToNBT(new CompoundTag()));
            Packs.BASE_CLIENT_PACK.getSender().send(SolarNETList.SYNC_T_BLOCK, pos, tag);
        }
    }

    /**
     * 使得放置物具有手上物品tank的读取
     */
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity == null) return;
        FluidUtil.setTank(blockEntity, stack);
    }

    /**
     * 使得复制物具有该实体的tank内容
     */
    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = super.getCloneItemStack(level, pos, state);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity == null) return stack;
        FluidUtil.setTank(stack, blockEntity);
        return stack;
    }

    /**
     * 让战利品表的同类掉落物存在tank信息
     */
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        ItemStack stack = new ItemStack(this);
        if(blockEntity != null) {
            FluidUtil.setTank(stack, blockEntity);
            return Collections.singletonList(stack);
        }
        return super.getDrops(state, builder);
    }

    /**
     * 设定红石信号逻辑
     * 按液体比例输出信号
     */
    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BaseTankBlockEntity t) {
            return (int) (FluidUtil.getScale(t.tank) * 15);
        }
        return 0;
    }

}
