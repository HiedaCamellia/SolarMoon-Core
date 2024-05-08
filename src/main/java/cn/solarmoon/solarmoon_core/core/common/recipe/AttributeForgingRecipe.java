package cn.solarmoon.solarmoon_core.core.common.recipe;

import cn.solarmoon.solarmoon_core.api.common.recipe.IConcreteRecipe;
import cn.solarmoon.solarmoon_core.api.common.recipe.serializable.AttributeData;
import cn.solarmoon.solarmoon_core.api.common.registry.RecipeEntry;
import cn.solarmoon.solarmoon_core.api.util.ItemStackUtil;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

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

}
