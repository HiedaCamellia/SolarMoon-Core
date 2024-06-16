package cn.solarmoon.solarmoon_core.feature.capability;

import cn.solarmoon.solarmoon_core.api.capability.HiddenItemInsertionData;
import cn.solarmoon.solarmoon_core.api.optional_recipe_item.RecipeSelectorData;
import cn.solarmoon.solarmoon_core.feature.embedding.EmbeddingData;

public interface IItemStackData {

    EmbeddingData getEmbeddingData();
    RecipeSelectorData getRecipeSelectorData();
    HiddenItemInsertionData getHiddenItemInsertionData();

}
