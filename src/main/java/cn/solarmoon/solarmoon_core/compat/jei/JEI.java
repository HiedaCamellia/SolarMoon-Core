package cn.solarmoon.solarmoon_core.compat.jei;

import cn.solarmoon.solarmoon_core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.api.compat.jei.BaseJEI;
import cn.solarmoon.solarmoon_core.api.renderer.ClipResList;
import cn.solarmoon.solarmoon_core.compat.jei.category.AttributeForgingCategory;
import cn.solarmoon.solarmoon_core.compat.jei.category.UseCategory;
import cn.solarmoon.solarmoon_core.feature.embedding.AttributeForgingRecipe;
import cn.solarmoon.solarmoon_core.feature.generic_recipe.use.UseRecipe;
import cn.solarmoon.solarmoon_core.registry.common.SolarRecipes;
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
                        .icon(guiHelper.createDrawable(ClipResList.JEI_HAND_POINT, 0, 0, 14, 11))
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
