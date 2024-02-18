package cn.solarmoon.solarmoon_core.network.handler;


import cn.solarmoon.solarmoon_core.common.capability.serializable.RecipeSelectorData;
import cn.solarmoon.solarmoon_core.common.block_entity.IContainerBlockEntity;
import cn.solarmoon.solarmoon_core.common.block_entity.ITankBlockEntity;
import cn.solarmoon.solarmoon_core.common.block_entity.iutor.ITimeRecipeBlockEntity;
import cn.solarmoon.solarmoon_core.network.IClientPackHandler;
import cn.solarmoon.solarmoon_core.registry.SolarCapabilities;
import cn.solarmoon.solarmoon_core.util.CapabilityUtil;
import cn.solarmoon.solarmoon_core.util.namespace.SolarNBTList;
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
    public void handle(LocalPlayer player, ClientLevel level, BlockPos pos, ItemStack stack, CompoundTag tag, float f, int[] ints, String string, List<ItemStack> stacks, String message) {
        switch (message) {
            case SolarNETList.SYNC_FURNACE -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof AbstractFurnaceBlockEntity e) {
                    for(int i = 0; i < stacks.size(); i++) {
                        e.setItem(i, stacks.get(i));
                    }
                }
            }
            case SolarNETList.SYNC_IC_BLOCK -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if(blockEntity instanceof IContainerBlockEntity containerTile) {
                    containerTile.setInventory(tag);
                }
            }
            case SolarNETList.SYNC_IT_BLOCK -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if(blockEntity instanceof ITankBlockEntity tankTile) {
                    tankTile.setFluid(tag);
                }
            }
            case SolarNETList.SYNC_IRT_BLOCK -> {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof ITimeRecipeBlockEntity<?> timeTile) {
                    timeTile.setTime(tag.getInt(SolarNBTList.TIME));
                }
            }
            case SolarNETList.SYNC_INDEX -> {
                RecipeSelectorData selector = CapabilityUtil.getData(player, SolarCapabilities.PLAYER_DATA).getRecipeSelectorData();
                RecipeType<?> recipeType = ForgeRegistries.RECIPE_TYPES.getValue(new ResourceLocation(string));
                selector.setIndex((int) f, recipeType);
            }
            case SolarNETList.SYNC_RECIPE_INDEX -> {
                RecipeSelectorData selector = CapabilityUtil.getData(player, SolarCapabilities.PLAYER_DATA).getRecipeSelectorData();
                RecipeType<?> recipeType = ForgeRegistries.RECIPE_TYPES.getValue(new ResourceLocation(string));
                selector.setRecipeIndex((int) f, recipeType);
            }
        }
    }

}
