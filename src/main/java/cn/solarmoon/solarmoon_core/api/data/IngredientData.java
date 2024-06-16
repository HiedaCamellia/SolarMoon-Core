package cn.solarmoon.solarmoon_core.api.data;

import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

public class IngredientData {

    @SerializedName("item")
    public String item;

    @SerializedName("tag")
    public String tag;

    public Item getItem() {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(item));
    }

    public TagKey<Item> getItemTag() {
        return TagKey.create(Registries.ITEM, new ResourceLocation(tag));
    }

    public Ingredient get() {
        validate();
        if (item != null) {
            return Ingredient.of(getItem());
        } else if (tag != null) {
            return Ingredient.of(getItemTag());
        }
        throw new JsonParseException("The obtained ingredient is invalid");
    }

    public void validate() throws JsonParseException {
        if (item == null && tag == null) throw new JsonParseException("You have to fill in an item or a tag.");
        if (item != null && tag != null) throw new JsonParseException("You can only choose between an item and a tag.");
    }

    @Override
    public String toString() {
        return "IngredientData{" +
                "item='" + item + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}

