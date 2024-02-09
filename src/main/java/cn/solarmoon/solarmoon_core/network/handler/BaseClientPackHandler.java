package cn.solarmoon.solarmoon_core.network.handler;


import cn.solarmoon.solarmoon_core.common.capability.serializable.RecipeSelectorData;
import cn.solarmoon.solarmoon_core.common.entity_block.entity.BaseContainerBlockEntity;
import cn.solarmoon.solarmoon_core.common.entity_block.entity.BaseTCBlockEntity;
import cn.solarmoon.solarmoon_core.common.entity_block.entity.BaseTankBlockEntity;
import cn.solarmoon.solarmoon_core.network.IClientPackHandler;
import cn.solarmoon.solarmoon_core.registry.Capabilities;
import cn.solarmoon.solarmoon_core.util.CapabilityUtil;
import cn.solarmoon.solarmoon_core.util.namespace.SolarNETList;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class BaseClientPackHandler implements IClientPackHandler {

    @Override
    public void handle(LocalPlayer player, ClientLevel level, BlockPos pos, CompoundTag tag, float f, String string, List<ItemStack> stacks, String message) {
        switch (message) {
            case SolarNETList.SYNC_FURNACE -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof AbstractFurnaceBlockEntity e) {
                    for(int i = 0; i < stacks.size(); i++) {
                        e.setItem(i, stacks.get(i));
                    }
                }
            }
            case SolarNETList.SYNC_C_BLOCK -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if(blockEntity instanceof BaseContainerBlockEntity c) {
                    c.setInventory(tag);
                }
            }
            case SolarNETList.SYNC_T_BLOCK -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if(blockEntity instanceof BaseTankBlockEntity tankBlockEntity) {
                    tankBlockEntity.setFluid(tag);
                }
            }
            case SolarNETList.SYNC_TC_BLOCK -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if(blockEntity instanceof BaseTCBlockEntity tc) {
                    tc.setFluid(tag);
                    tc.setInventory(tag);
                }
            }
            case SolarNETList.SYNC_INDEX -> {
                RecipeSelectorData selector = CapabilityUtil.getData(player, Capabilities.PLAYER_DATA).getRecipeSelectorData();
                RecipeType<?> recipeType = ForgeRegistries.RECIPE_TYPES.getValue(new ResourceLocation(string));
                selector.setIndex((int) f, recipeType);
            }
            case SolarNETList.SYNC_RECIPE_INDEX -> {
                RecipeSelectorData selector = CapabilityUtil.getData(player, Capabilities.PLAYER_DATA).getRecipeSelectorData();
                RecipeType<?> recipeType = ForgeRegistries.RECIPE_TYPES.getValue(new ResourceLocation(string));
                selector.setRecipeIndex((int) f, recipeType);
            }
        }
    }

}
