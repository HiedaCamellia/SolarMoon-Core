package cn.solarmoon.solarmoon_core.api.optional_recipe_item;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;

/**
 * 配方选择数据，拥有对应方块->数字的hashmap，一般用于IOptionalRecipeItem的配方滚轮选取
 */
public class RecipeSelectorData implements INBTSerializable<CompoundTag> {

    private final HashMap<Block, Integer> indexOfBlockInput;

    public RecipeSelectorData() {
        this.indexOfBlockInput = new HashMap<>();
    }

    public void setIndex(int index, Block block) {
        indexOfBlockInput.put(block, index);
    }

    public int getIndex(Block block) {
        if (indexOfBlockInput.get(block) == null) return 0;
        return indexOfBlockInput.get(block);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        Gson gson = new Gson();

        JsonArray jsonArrayIndexOfRecipe = new JsonArray();
        for (var entry : indexOfBlockInput.entrySet()) {
            JsonObject jsonObject = new JsonObject();
            ResourceLocation key = ForgeRegistries.BLOCKS.getKey(entry.getKey());
            if (key != null) {
                jsonObject.addProperty("id", key.toString());
                jsonObject.addProperty("value", entry.getValue());
                jsonArrayIndexOfRecipe.add(jsonObject);
            }
        }
        tag.putString("indexOfRecipe", gson.toJson(jsonArrayIndexOfRecipe));

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
                Block block = ForgeRegistries.BLOCKS.getValue(res);
                indexOfBlockInput.put(block, value);
            }
        }

    }

}
