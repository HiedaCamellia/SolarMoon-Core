package cn.solarmoon.solarmoon_core.common.entity_block.entity;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

/**
 * 基础的容器类，省去了设置inventory的功夫，可以不用但不能没有！
 */
public abstract class BaseContainerBlockEntity extends BlockEntity implements IContainerBlockEntity {

    private final ItemStackHandler inventory;

    public BaseContainerBlockEntity(BlockEntityType<?> type, int size, int slotLimit, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.inventory = new ItemStackHandler(size) {
            @Override
            public int getSlotLimit(int slot) {
                return slotLimit;
            }
        };
    }

    public BaseContainerBlockEntity(BlockEntityType<?> type, int size, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.inventory = new ItemStackHandler(size);
    }

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }

}
