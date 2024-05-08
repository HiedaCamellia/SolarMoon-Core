package cn.solarmoon.solarmoon_core.core.common.recipe.serializer;

import cn.solarmoon.solarmoon_core.api.common.recipe.serializable.AttributeData;
import cn.solarmoon.solarmoon_core.core.common.recipe.AttributeForgingRecipe;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

public class AttributeForgingRecipeSerializer implements RecipeSerializer<AttributeForgingRecipe> {

    @Override
    public AttributeForgingRecipe fromJson(ResourceLocation id, JsonObject json) {
        Ingredient input = Ingredient.fromJson(json.get("input"));
        ItemStack material = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "material"), true);
        AttributeData attributeData = AttributeData.deserialize(GsonHelper.getAsJsonObject(json, "attribute"));
        int expCost = GsonHelper.getAsInt(json, "cost");
        int maxForgeCount = GsonHelper.getAsInt(json, "max_forging_count", 0);
        return new AttributeForgingRecipe(id, input, material, attributeData, expCost, maxForgeCount);
    }

    @Override
    public AttributeForgingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
        Ingredient input = Ingredient.fromNetwork(buffer);
        ItemStack material = buffer.readItem();
        AttributeData attributeData = AttributeData.read(buffer);
        int expCost = buffer.readInt();
        int maxForgeCount = buffer.readInt();
        return new AttributeForgingRecipe(id, input, material, attributeData, expCost, maxForgeCount);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, AttributeForgingRecipe recipe) {
        recipe.input().toNetwork(buffer);
        buffer.writeItem(recipe.material());
        recipe.attributeData().write(buffer);
        buffer.writeInt(recipe.expCost());
        buffer.writeInt(recipe.maxForgeCount());
    }


}
