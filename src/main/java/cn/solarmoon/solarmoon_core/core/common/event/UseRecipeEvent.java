package cn.solarmoon.solarmoon_core.core.common.event;

import cn.solarmoon.solarmoon_core.api.util.*;
import cn.solarmoon.solarmoon_core.core.common.recipe.UseRecipe;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Optional;

public class UseRecipeEvent {

    @SubscribeEvent
    public void use(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        ItemStack heldItem = event.getItemStack();
        BlockPos pos = event.getPos();
        Level level = event.getLevel();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        InteractionHand hand = event.getHand();

        List<UseRecipe> recipes = level.getRecipeManager().getAllRecipesFor(SolarRecipes.USE.get());
        Optional<UseRecipe> recipeOptional = recipes.stream().filter(recipe ->
                block == recipe.inputBlock() && recipe.ingredient().test(heldItem)
                ).findFirst();
        if (recipeOptional.isPresent()) {
            UseRecipe recipe = recipeOptional.get();
            if (block != recipe.outputBlock()) BlockUtil.replaceBlockWithAllState(state, recipe.outputBlock().defaultBlockState(), level, pos);
            if (!player.isCreative()) {
                LevelSummonUtil.addItemToInventory(player, heldItem.getCraftingRemainingItem());
                heldItem.shrink(1);
            }
            recipe.getRolledResults(player).forEach(c -> LevelSummonUtil.summonDrop(c, level, pos));
            player.swing(hand);
            event.setCanceled(true);
        }
    }

}
