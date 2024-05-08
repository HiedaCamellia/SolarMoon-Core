package cn.solarmoon.solarmoon_core.core.common.recipe.serializer;

import cn.solarmoon.solarmoon_core.api.common.recipe.serializable.ChanceResult;
import cn.solarmoon.solarmoon_core.api.util.RecipeSerializeHelper;
import cn.solarmoon.solarmoon_core.core.common.recipe.UseRecipe;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class UseRecipeSerializer implements RecipeSerializer<UseRecipe> {

    @Override
    public UseRecipe fromJson(ResourceLocation id, JsonObject json) {
        Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
        Block inputBlock = RecipeSerializeHelper.readBlock(json, "input_block");
        Block outputBlock = RecipeSerializeHelper.readBlock(json, "output_block", inputBlock);
        NonNullList<ChanceResult> chanceResults = RecipeSerializeHelper.readChanceResults(json, "results");
        return new UseRecipe(id, ingredient, inputBlock, outputBlock, chanceResults);
    }

    @Override
    public @Nullable UseRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
        Ingredient ingredient = Ingredient.fromNetwork(buf);
        Block inputBlock = RecipeSerializeHelper.readBlock(buf);
        Block outputBlock = RecipeSerializeHelper.readBlock(buf);
        NonNullList<ChanceResult> chanceResults = RecipeSerializeHelper.readChanceResults(buf);
        return new UseRecipe(id, ingredient, inputBlock, outputBlock, chanceResults);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, UseRecipe recipe) {
        recipe.ingredient().toNetwork(buf);
        RecipeSerializeHelper.writeBlock(buf, recipe.inputBlock());
        RecipeSerializeHelper.writeBlock(buf, recipe.outputBlock());
        RecipeSerializeHelper.writeChanceResults(buf, recipe.chanceResults());
    }

}
