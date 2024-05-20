package cn.solarmoon.solarmoon_core.core.client.registry;

import cn.solarmoon.solarmoon_core.api.client.registry.BaseTooltipRegistry;
import cn.solarmoon.solarmoon_core.core.client.renderer.tooltip.EmbeddingTooltip;

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
