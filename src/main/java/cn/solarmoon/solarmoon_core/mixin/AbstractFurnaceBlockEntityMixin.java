package cn.solarmoon.solarmoon_core.mixin;


import cn.solarmoon.solarmoon_core.network.NETList;
import cn.solarmoon.solarmoon_core.registry.common.SolarNetPacks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(AbstractFurnaceBlockEntity.class)
public class AbstractFurnaceBlockEntityMixin {
    /**
     * 使得熔炉类物品可以随时从服务端同步内容物到客户端，以便渲染
     */
    @Inject(method = "serverTick", at = @At("HEAD"))
    private static void updateStack(Level level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
        List<ItemStack> stacks = new ArrayList<>();
        for(int i = 0 ; i < blockEntity.getContainerSize() ; i++) {
            ItemStack stack = blockEntity.getItem(i);
            stacks.add(stack);
        }
        SolarNetPacks.CLIENT.getSender()
                .pos(pos)
                .stacks(stacks)
                .send(NETList.SYNC_FURNACE);
    }
}
