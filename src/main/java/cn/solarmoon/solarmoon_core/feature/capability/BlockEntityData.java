package cn.solarmoon.solarmoon_core.feature.capability;

import cn.solarmoon.solarmoon_core.api.capability.anim_ticker.AnimTicker;
import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class BlockEntityData implements ICapabilitySerializable<CompoundTag>, IBlockEntityData {

    private final LazyOptional<IBlockEntityData> blockEntityData;
    private final BlockEntity blockEntity;

    private final HashMap<Integer, AnimTicker> animTickerMap;


    public BlockEntityData(BlockEntity blockEntity) {
        this.blockEntityData = LazyOptional.of(() -> this);
        this.blockEntity = blockEntity;

        animTickerMap = new HashMap<>();
    }

    public void tick() {
        for (var animTicker : animTickerMap.values()) {
            for (int i = 0; i < animTicker.getFactor(); i++) {
                animTicker.tick();
            }
        }
    }

    @Override
    public AnimTicker getAnimTicker(int index) {
        if (!animTickerMap.containsKey(index)) {
            animTickerMap.put(index, new AnimTicker(index, blockEntity));
        }
        return animTickerMap.get(index);
    }

    @Override
    public HashMap<Integer, AnimTicker> getAnimTickerMap() {
        return animTickerMap;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();

        ListTag listTag = new ListTag();
        for (Map.Entry<Integer, AnimTicker> entry : animTickerMap.entrySet()) {
            CompoundTag entryTag = new CompoundTag();
            entryTag.putInt("Key", entry.getKey());
            entryTag.put("Value", entry.getValue().serializeNBT());
            listTag.add(entryTag);
        }
        tag.put("AnimTickerMap", listTag);

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ListTag listTag = nbt.getList("AnimTickerMap", ListTag.TAG_COMPOUND);
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag entryTag = listTag.getCompound(i);
            int key = entryTag.getInt("Key");
            AnimTicker value = new AnimTicker(key, blockEntity);
            value.deserializeNBT(entryTag.getCompound("Value"));
            animTickerMap.put(key, value);
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == SolarCapabilities.BLOCK_ENTITY_DATA) {
            return blockEntityData.cast();
        }
        return LazyOptional.empty();
    }

}
