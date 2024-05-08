package cn.solarmoon.solarmoon_core.core.jei.category;

import cn.solarmoon.solarmoon_core.api.common.recipe.serializable.ChanceResult;
import cn.solarmoon.solarmoon_core.api.compat.jei.category.BaseJEICategory;
import cn.solarmoon.solarmoon_core.core.common.recipe.UseRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class UseCategory extends BaseJEICategory<UseRecipe> {

    private final int gridX = 76;
    private final int gridY = 10;

    public UseCategory(IGuiHelper helper) {
        super(helper);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, UseRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.ingredient());
        builder.addSlot(RecipeIngredientRole.INPUT, 43, 1).addItemStack(new ItemStack(recipe.inputBlock().asItem()));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 95, 1).addItemStack(new ItemStack(recipe.outputBlock().asItem()));
        NonNullList<ChanceResult> recipeOutputs = recipe.chanceResults();
    }

    @Override
    public void draw(UseRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        DEFAULT_SLOT.draw(guiGraphics, 94,0);
        EMPTY_ARROW.draw(guiGraphics, 66, 1);
        HAND_POINT.draw(guiGraphics, 23, 3);
    }

}
