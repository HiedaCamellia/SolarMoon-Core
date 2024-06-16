package cn.solarmoon.solarmoon_core.registry.client;

import cn.solarmoon.solarmoon_core.api.entry.client.BaseTooltipRegistry;
import cn.solarmoon.solarmoon_core.feature.embedding.EmbeddingTooltip;

public class SolarTooltips extends BaseTooltipRegistry {

    @Override
    public void addRegistry() {
        add(EmbeddingTooltip.TooltipComponent.class, EmbeddingTooltip::new);
    }

    @Override
    public void gatherComponents() {
        gatherToFirstEmpty(EmbeddingTooltip.TooltipComponent::new);
    }

}
