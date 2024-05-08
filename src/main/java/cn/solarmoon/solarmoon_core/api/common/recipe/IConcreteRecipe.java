package cn.solarmoon.solarmoon_core.api.common.recipe;

import cn.solarmoon.solarmoon_core.api.common.registry.RecipeEntry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

/**
 * 由于用到Container的情况较少（因为大多数物品都是直接被存在了本体数据中而非原版的方式），故简化一下此接口<br/>
 * 你的方法太多余了！ReRe！
 */
public interface IConcreteRecipe extends Recipe<RecipeWrapper> {

    RecipeEntry<?> getRecipeEntry();

    ResourceLocation getId();

    @Override
    default boolean matches(RecipeWrapper inv, Level level) {
        return false;
    }

    @Override
    default ItemStack getResultItem(RegistryAccess level) {
        return ItemStack.EMPTY;
    }

    @Override
    default ItemStack assemble(RecipeWrapper inv, RegistryAccess levelAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    default boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    default RecipeSerializer<?> getSerializer() {
        return getRecipeEntry().getSerializer();
    }

    @Override
    default RecipeType<?> getType() {
        return getRecipeEntry().get();
    }

}
