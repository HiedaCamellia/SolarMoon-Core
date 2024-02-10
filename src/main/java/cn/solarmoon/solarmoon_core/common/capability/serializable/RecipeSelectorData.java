package cn.solarmoon.solarmoon_core.common.capability.serializable;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;

/**
 * 配方选择数据，拥有对应配方->数字的hashmap，一般用于IOptionalRecipeItem的配方滚轮选取
 */
public class RecipeSelectorData implements INBTSerializable<CompoundTag> {

    private final HashMap<RecipeType<?>, Integer> indexOfRecipe;
    private final HashMap<RecipeType<?>, Integer> recipeIndexOfRecipe;

    public RecipeSelectorData() {
        this.indexOfRecipe = new HashMap<>();
        this.recipeIndexOfRecipe = new HashMap<>();
    }

    public void setIndex(int index, RecipeType<?> recipeType) {
        indexOfRecipe.put(recipeType, index);
    }

    public int getIndex(RecipeType<?> recipeType) {
        if (indexOfRecipe.get(recipeType) == null) return 0;
        return indexOfRecipe.get(recipeType);
    }

    public void setRecipeIndex(int recipeIndex, RecipeType<?> recipeType) {
        recipeIndexOfRecipe.put(recipeType, recipeIndex);
    }

    public int getRecipeIndex(RecipeType<?> recipeType) {
        if (recipeIndexOfRecipe.get(recipeType) == null) return 0;
        return recipeIndexOfRecipe.get(recipeType);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        Gson gson = new Gson();

        JsonArray jsonArrayIndexOfRecipe = new JsonArray();
        for (var entry : indexOfRecipe.entrySet()) {
            if (entry.getKey() == null) continue;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", entry.getKey().toString());
            jsonObject.addProperty("value", entry.getValue());
            jsonArrayIndexOfRecipe.add(jsonObject);
        }
        tag.putString("indexOfRecipe", gson.toJson(jsonArrayIndexOfRecipe));

        JsonArray jsonArrayRecipeIndexOfRecipe = new JsonArray();
        for (var entry : recipeIndexOfRecipe.entrySet()) {
            if (entry.getKey() == null) continue;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", entry.getKey().toString());
            jsonObject.addProperty("value", entry.getValue());
            jsonArrayRecipeIndexOfRecipe.add(jsonObject);
        }
        tag.putString("recipeIndexOfRecipe", gson.toJson(jsonArrayRecipeIndexOfRecipe));

        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        Gson gson = new Gson();

        JsonArray jsonArrayIndexOfRecipe = gson.fromJson(nbt.getString("indexOfRecipe"), JsonArray.class);
        if (jsonArrayIndexOfRecipe != null) {
            for (var jsonElement : jsonArrayIndexOfRecipe) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String id = jsonObject.get("id").getAsString();
                int value = jsonObject.get("value").getAsInt();
                ResourceLocation res = new ResourceLocation(id);
                RecipeType<?> recipeType = ForgeRegistries.RECIPE_TYPES.getValue(res);
                indexOfRecipe.put(recipeType, value);
            }
        }

        JsonArray jsonArrayRecipeIndexOfRecipe = gson.fromJson(nbt.getString("recipeIndexOfRecipe"), JsonArray.class);
        if (jsonArrayRecipeIndexOfRecipe != null) {
            for (var jsonElement : jsonArrayRecipeIndexOfRecipe) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String id = jsonObject.get("id").getAsString();
                int value = jsonObject.get("value").getAsInt();
                ResourceLocation res = new ResourceLocation(id);
                RecipeType<?> recipeType = ForgeRegistries.RECIPE_TYPES.getValue(res);
                recipeIndexOfRecipe.put(recipeType, value);
            }
        }

    }

}
