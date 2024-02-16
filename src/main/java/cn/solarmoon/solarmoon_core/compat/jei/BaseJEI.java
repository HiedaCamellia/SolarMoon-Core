package cn.solarmoon.solarmoon_core.compat.jei;

import cn.solarmoon.solarmoon_core.compat.jei.category.BaseJEICategory;
import cn.solarmoon.solarmoon_core.compat.jei.element.EmptyBackground;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 简便的jei创建器，需要自己添加@JeiPlugin标识以启用
 */
@SuppressWarnings("unchecked")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class BaseJEI implements IModPlugin {

    protected IGuiHelper guiHelper;
    private final List<JEIBuilder> jeiBuilders;

    public BaseJEI() {
        this.jeiBuilders = new ArrayList<>();
    }

    public abstract void register();

    public abstract String getModId();

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        guiHelper = registry.getJeiHelpers().getGuiHelper();
        register();
        for (JEIBuilder jeiBuilder : jeiBuilders) {
            registry.addRecipeCategories(jeiBuilder.getRecipeCategory());
        }
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        for (JEIBuilder jeiBuilder : jeiBuilders) {
            registration.addRecipes(jeiBuilder.getRecipeType(), jeiBuilder.getClientRecipes());
        }
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        for (JEIBuilder jeiBuilder : jeiBuilders) {
            for (ItemStack stack : jeiBuilder.getCatalystStacks()) {
                registration.addRecipeCatalyst(stack, jeiBuilder.getRecipeType());
            }
        }
    }

    public void add(JEIBuilder... jeiBuilder) {
        jeiBuilders.addAll(Arrays.asList(jeiBuilder));
    }

    public <T extends Recipe<?>> JEIBuilder builder(Class<? extends T> recipeClass) {
        return new JEIBuilder(recipeClass, getModId(), guiHelper);
    }

    public static class JEIBuilder {

        private final Class<? extends Recipe<?>> recipeClass;

        private RecipeType<?> recipeType;
        private IRecipeCategory<?> recipeCategory;
        private net.minecraft.world.item.crafting.RecipeType<?> mcRecipeType;
        private Component title;
        private IDrawable background;
        private IDrawable icon;
        private final String modId;
        private final IGuiHelper guiHelper;
        private ItemStack iconItem;

        private final List<ItemStack> catalystStacks;

        public <T extends Recipe<?>> JEIBuilder(Class<? extends T> recipeClass, String modId, IGuiHelper guiHelper) {
            this.recipeClass = recipeClass;
            this.modId = modId;
            this.guiHelper = guiHelper;
            this.catalystStacks = new ArrayList<>();
        }

        /**
         * 把配方绑定在物品上
         */
        public JEIBuilder addRecipeCatalyst(ItemStack itemStack) {
            catalystStacks.add(itemStack);
            return this;
        }

        /**
         * 把配方绑定在物品上
         */
        public JEIBuilder addRecipeCatalyst(ItemLike item) {
            catalystStacks.add(new ItemStack(item));
            return this;
        }

        /**
         * 把配方绑定在物品上，必填
         */
        public JEIBuilder addRecipeCatalyst(Ingredient ingredient) {
            catalystStacks.addAll(Arrays.asList(ingredient.getItems()));
            return this;
        }

        /**
         * 把该配方类型的所有配方与jei绑定
         */
        public <T extends Recipe<?>> JEIBuilder recipeType(net.minecraft.world.item.crafting.RecipeType<T> recipeType) {
            this.mcRecipeType = recipeType;
            return this;
        }

        public JEIBuilder background(IDrawable background) {
            this.background = background;
            return this;
        }

        public JEIBuilder emptyBackground(int width, int height) {
            background(new EmptyBackground(width, height));
            return this;
        }

        /**
         * 不填默认输出icon显示的物品的名称，icon也没有就显示绑定的第一个物品名称
         */
        public JEIBuilder title(Component title) {
            this.title = title;
            return this;
        }

        /**
         * 不填默认输出绑定的物品中的第一个物品的样式
         */
        public JEIBuilder icon(IDrawable icon) {
            this.icon = icon;
            return this;
        }

        /**
         * 不填默认输出绑定的物品中的第一个物品的样式
         */
        public JEIBuilder icon(ItemLike item) {
            icon(guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(item)));
            this.iconItem = new ItemStack(item);
            return this;
        }

        /**
         * 最终构建，不是添加配方类型无需构建
         */
        public JEIBuilder build(String id, IRecipeCategory<?> recipeCategory) {
            recipeType = RecipeType.create(modId, id, recipeClass);
            if (title == null) {
                if (iconItem != null) title = iconItem.getHoverName();
                else if (!catalystStacks.isEmpty()) title = catalystStacks.get(0).getHoverName();
            }
            if (icon == null) {
                if (!catalystStacks.isEmpty()) icon(catalystStacks.get(0).getItem());
            }
            if (recipeCategory instanceof BaseJEICategory<?> jeiCategory) {
                this.recipeCategory = jeiCategory.setJeiBuilder(this);
            }
            else this.recipeCategory = recipeCategory;
            return this;
        }

        public IRecipeCategory<?> getRecipeCategory() {
            return recipeCategory;
        }

        public List<ItemStack> getCatalystStacks() {
            return catalystStacks;
        }

        public <T> RecipeType<T> getRecipeType() {
            return (RecipeType<T>) recipeType;
        }

        public IDrawable getBackground() {
            return background;
        }

        public Component getTitle() {
            return title;
        }

        public IDrawable getIcon() {
            return icon;
        }

        public IGuiHelper getGuiHelper() {
            return guiHelper;
        }

        public <T extends Recipe<RecipeWrapper>> List<T> getClientRecipes() {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
                return mc.level.getRecipeManager().getAllRecipesFor((net.minecraft.world.item.crafting.RecipeType<T>) mcRecipeType);
            }
            return new ArrayList<>();
        }

    }

}
