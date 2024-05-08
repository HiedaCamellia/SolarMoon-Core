package cn.solarmoon.solarmoon_core.api.common.capability;

import cn.solarmoon.solarmoon_core.api.common.capability.serializable.itemstack.EmploymentData;
import cn.solarmoon.solarmoon_core.api.common.capability.serializable.itemstack.EmbeddingData;
import cn.solarmoon.solarmoon_core.api.common.capability.serializable.itemstack.RecipeSelectorData;

public interface IItemStackData {

    EmploymentData getEmploymentData();
    EmbeddingData getEmbeddingData();
    RecipeSelectorData getRecipeSelectorData();

}
