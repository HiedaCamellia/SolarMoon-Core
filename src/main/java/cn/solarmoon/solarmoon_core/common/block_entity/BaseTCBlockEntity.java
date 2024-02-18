package cn.solarmoon.solarmoon_core.common.block_entity;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;

/**
 * 禁忌の液体物品容器双重存在<br/>
 * 哪里有问题直接看上一级即可，只是简单合并了一下
 */
public abstract class BaseTCBlockEntity extends BaseTankBlockEntity implements IContainerBlockEntity {

    private final ItemStackHandler inventory;

    public BaseTCBlockEntity(BlockEntityType<?> type, int maxCapacity, int size, int slotLimit, BlockPos pos, BlockState state) {
        super(type, maxCapacity, pos, state);
        this.inventory = new ItemStackHandler(size) {
            @Override
            public int getSlotLimit(int slot) {
                return slotLimit;
            }
        };
    }

    public BaseTCBlockEntity(BlockEntityType<?> type, int maxCapacity, int size, BlockPos pos, BlockState state) {
        super(type, maxCapacity, pos, state);
        this.inventory = new ItemStackHandler(size);
    }

    @Override
    public ItemStackHandler getInventory() {
        return inventory;
    }


}
