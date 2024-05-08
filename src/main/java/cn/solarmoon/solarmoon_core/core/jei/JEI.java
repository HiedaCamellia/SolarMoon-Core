package cn.solarmoon.solarmoon_core.core.jei;

import cn.solarmoon.solarmoon_core.core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.api.compat.jei.BaseJEI;
import cn.solarmoon.solarmoon_core.api.util.namespace.SolarBaseRes;
import cn.solarmoon.solarmoon_core.core.common.recipe.AttributeForgingRecipe;
import cn.solarmoon.solarmoon_core.core.common.recipe.UseRecipe;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarRecipes;
import cn.solarmoon.solarmoon_core.core.jei.category.AttributeForgingCategory;
import cn.solarmoon.solarmoon_core.core.jei.category.UseCategory;
import mezz.jei.api.JeiPlugin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

@JeiPlugin
public class JEI extends BaseJEI {

    @Override
    public void register() {
        add(
                builder()
                        .boundCategory(new AttributeForgingCategory(guiHelper))
                        .recipeType(SolarRecipes.ATTRIBUTE_FORGING.get())
                        .addRecipeCatalyst(Ingredient.of(ItemTags.ANVIL))
                        .icon(Items.ANVIL)
                        .emptyBackground(112, 18)
                        .title(SolarMoonCore.TRANSLATOR.set("jei", "attribute_forging.title"))
                        .build("attribute_forging", AttributeForgingRecipe.class),
                builder()
                        .boundCategory(new UseCategory(guiHelper))
                        .recipeType(SolarRecipes.USE.get())
                        .icon(guiHelper.createDrawable(SolarBaseRes.JEI_HAND_POINT, 0, 0, 14, 11))
                        .emptyBackground(112, 18)
                        .title(SolarMoonCore.TRANSLATOR.set("jei", "use.title"))
                        .build("use", UseRecipe.class)
        );
    }

    @Override
    public String getModId() {
        return SolarMoonCore.MOD_ID;
    }

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(getModId());
    }

}
