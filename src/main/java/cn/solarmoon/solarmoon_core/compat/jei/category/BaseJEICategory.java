package cn.solarmoon.solarmoon_core.compat.jei.category;

import cn.solarmoon.solarmoon_core.compat.jei.BaseJEI;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.chat.Component;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * 完全简化后的jei界面渲染
 * @param <T> 配方类型
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class BaseJEICategory<T> implements IRecipeCategory<T> {

    private BaseJEI.JEIBuilder jeiBuilder;

    public BaseJEICategory() {}

    public IGuiHelper getGuiHelper() {
        return jeiBuilder.getGuiHelper();
    }

    @Override
    public RecipeType<T> getRecipeType() {
        return jeiBuilder.getRecipeType();
    }

    @Override
    public Component getTitle() {
        return jeiBuilder.getTitle();
    }

    @Override
    public IDrawable getBackground() {
        return jeiBuilder.getBackground();
    }

    @Override
    public IDrawable getIcon() {
        return jeiBuilder.getIcon();
    }

    public BaseJEICategory<T> setJeiBuilder(BaseJEI.JEIBuilder jeiBuilder) {
        this.jeiBuilder = jeiBuilder;
        return this;
    }

}
