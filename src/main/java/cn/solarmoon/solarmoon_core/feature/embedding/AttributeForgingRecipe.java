package cn.solarmoon.solarmoon_core.feature.embedding;

import cn.solarmoon.solarmoon_core.api.data.SerializeHelper;
import cn.solarmoon.solarmoon_core.api.entry.common.RecipeEntry;
import cn.solarmoon.solarmoon_core.api.item_util.ItemStackUtil;
import cn.solarmoon.solarmoon_core.api.recipe.AttributeData;
import cn.solarmoon.solarmoon_core.api.recipe.IConcreteRecipe;
import cn.solarmoon.solarmoon_core.registry.common.SolarRecipes;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.crafting.CraftingHelper;

import java.util.UUID;

public record AttributeForgingRecipe(
        ResourceLocation id,
        Ingredient input,
        ItemStack material, //这里以后由ComparableItemStack替代
        AttributeData attributeData,
        int expCost,
        int maxForgeCount
) implements IConcreteRecipe {

    public Attribute getAttribute() {
        return attributeData.attribute();
    }

    public AttributeModifier getAttributeModifier() {
        return attributeData.attributeModifier();
    }

    public double getAddValue() {
        return attributeData.attributeModifier().getAmount();
    }

    public UUID getUUID() {
        return attributeData.attributeModifier().getId();
    }

    public String getName() {
        return attributeData.attributeModifier().getName();
    }

    public AttributeModifier.Operation getOperation() {
        return attributeData.attributeModifier().getOperation();
    }

    /**
     * @return 是否能无限嵌入该消耗物
     */
    public boolean isForgingLimitless() {
        return maxForgeCount == 0;
    }

    /**
     * @param stack 作比较的物品栈
     * @return 输入的物品栈是否足够配方的消耗
     */
    public boolean isMaterialSufficient(ItemStack stack) {
        return isForgingLimitless() ? stack.is(material.getItem()) : ItemStackUtil.isSameAndSufficient(stack, material, false);
    }

    @Override
    public RecipeEntry<?> getRecipeEntry() {
        return SolarRecipes.ATTRIBUTE_FORGING;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public static class Serializer implements RecipeSerializer<AttributeForgingRecipe> {

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
            ItemStack material = SerializeHelper.readItemStack(buffer);
            AttributeData attributeData = AttributeData.read(buffer);
            int expCost = buffer.readInt();
            int maxForgeCount = buffer.readInt();
            return new AttributeForgingRecipe(id, input, material, attributeData, expCost, maxForgeCount);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, AttributeForgingRecipe recipe) {
            recipe.input().toNetwork(buffer);
            SerializeHelper.writeItemStack(buffer, recipe.material());
            recipe.attributeData().write(buffer);
            buffer.writeInt(recipe.expCost());
            buffer.writeInt(recipe.maxForgeCount());
        }


    }
}
