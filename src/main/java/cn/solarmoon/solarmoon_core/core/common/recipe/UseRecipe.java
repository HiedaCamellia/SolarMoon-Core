package cn.solarmoon.solarmoon_core.core.common.recipe;

import cn.solarmoon.solarmoon_core.api.common.recipe.IConcreteRecipe;
import cn.solarmoon.solarmoon_core.api.common.recipe.serializable.ChanceResult;
import cn.solarmoon.solarmoon_core.api.common.registry.RecipeEntry;
import cn.solarmoon.solarmoon_core.api.util.RecipeUtil;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

import java.util.List;

public record UseRecipe(
        ResourceLocation id,
        Ingredient ingredient,
        Block inputBlock,
        Block outputBlock,
        NonNullList<ChanceResult> chanceResults
) implements IConcreteRecipe {

    public List<ItemStack> getResults() {
        return RecipeUtil.getResults(chanceResults);
    }

    public List<ItemStack> getRolledResults(Player player) {
        return RecipeUtil.getRolledResults(player, chanceResults);
    }

    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return SolarRecipes.USE;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

}
