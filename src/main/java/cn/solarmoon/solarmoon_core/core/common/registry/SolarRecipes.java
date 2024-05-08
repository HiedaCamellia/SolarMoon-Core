package cn.solarmoon.solarmoon_core.core.common.registry;

import cn.solarmoon.solarmoon_core.core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.api.common.registry.RecipeEntry;
import cn.solarmoon.solarmoon_core.core.common.recipe.AttributeForgingRecipe;
import cn.solarmoon.solarmoon_core.core.common.recipe.UseRecipe;
import cn.solarmoon.solarmoon_core.core.common.recipe.serializer.AttributeForgingRecipeSerializer;
import cn.solarmoon.solarmoon_core.core.common.recipe.serializer.UseRecipeSerializer;

public class SolarRecipes {
    public static void register() {}

    public static final RecipeEntry<AttributeForgingRecipe> ATTRIBUTE_FORGING = SolarMoonCore.REGISTRY.recipe()
            .id("attribute_forging")
            .serializer(AttributeForgingRecipeSerializer::new)
            .build();

    public static final RecipeEntry<UseRecipe> USE = SolarMoonCore.REGISTRY.recipe()
            .id("use")
            .serializer(UseRecipeSerializer::new)
            .build();

}
