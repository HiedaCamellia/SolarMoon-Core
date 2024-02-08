package cn.solarmoon.solarmoon_core.common.entity_block;

import cn.solarmoon.solarmoon_core.common.entity_block.entity.BaseContainerBlockEntity;
import cn.solarmoon.solarmoon_core.common.entity_block.entity.BaseTCBlockEntity;
import cn.solarmoon.solarmoon_core.registry.Packs;
import cn.solarmoon.solarmoon_core.util.ContainerUtil;
import cn.solarmoon.solarmoon_core.util.LevelSummonUtil;
import cn.solarmoon.solarmoon_core.util.namespace.NBTList;
import cn.solarmoon.solarmoon_core.util.namespace.NETList;
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

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public abstract class BaseContainerEntityBlock extends BasicEntityBlock {

    public BaseContainerEntityBlock(Properties properties) {
        super(properties);
    }

    /**
     * 强制继承类实现use，因为容器一般需要特殊功能
     */
    @Override
    public abstract InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult);

    /**
     * 基本的存物逻辑，似乎可通用
     * 手不为空时存入，为空时疯狂取出
     */
    public boolean storage(BlockEntity blockEntity, Player player, InteractionHand hand) {
        if (blockEntity instanceof BaseTCBlockEntity tc) {
            ItemStack heldItem = player.getItemInHand(hand);
            if (!heldItem.isEmpty()) {
                ItemStack result = tc.insertItem(heldItem);
                player.setItemInHand(hand, result);
                tc.setChanged();
                return !result.equals(heldItem);
            } else {
                ItemStack result = tc.extractItem();
                LevelSummonUtil.addItemToInventory(player, result);
                tc.setChanged();
                return !result.equals(heldItem);
            }
        }
        return false;
    }

    /**
     * 默认进行流体和容器的同步
     */
    @Override
    public void tick(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        //防止放入物品未在客户端同步 而 造成的 假右键操作
        if (blockEntity instanceof BaseContainerBlockEntity c) {
            CompoundTag tag = new CompoundTag();
            tag.put(NBTList.INVENTORY, c.inventory.serializeNBT());
            Packs.BASE_CLIENT_PACK.getSender().send(NETList.SYNC_C_BLOCK, pos, tag);
        }
    }

    /**
     * 使得放置物具有手上物品tank的读取
     * 以及item内的item的读取
     */
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if(blockEntity == null) return;
        if(blockEntity instanceof BaseContainerBlockEntity c) {
            c.setInventory(stack);
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
        if(blockEntity == null) return stack;
        ContainerUtil.setInventory(stack, blockEntity);
        return stack;
    }

    /**
     * 让战利品表的同类掉落物存在tank信息
     * 和内容物信息
     */
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        ItemStack stack = new ItemStack(this);
        if(blockEntity != null) {
            ContainerUtil.setInventory(stack, blockEntity);
            return Collections.singletonList(stack);
        }
        return super.getDrops(state, builder);
    }

    /**
     * 设定红石信号逻辑
     * 按液体或物品比例输出信号
     * 两者皆有的情况下优先判断流体
     */
    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BaseContainerBlockEntity ct) {
            return ( (ct.getStacks().size() + 1) / ct.maxStackCount() ) * 15;
        }
        return 0;
    }

}
