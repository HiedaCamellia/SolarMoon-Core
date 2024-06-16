package cn.solarmoon.solarmoon_core.registry.common;

import cn.solarmoon.solarmoon_core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.api.entry.common.RecipeEntry;
import cn.solarmoon.solarmoon_core.feature.embedding.AttributeForgingRecipe;
import cn.solarmoon.solarmoon_core.feature.generic_recipe.use.UseRecipe;

public class SolarRecipes {
    public static void register() {}

    public static final RecipeEntry<AttributeForgingRecipe> ATTRIBUTE_FORGING = SolarMoonCore.REGISTRY.recipe()
            .id("attribute_forging")
            .serializer(AttributeForgingRecipe.Serializer::new)
            .build();

    public static final RecipeEntry<UseRecipe> USE = SolarMoonCore.REGISTRY.recipe()
            .id("use")
            .serializer(UseRecipe.Serializer::new)
            .build();

}
