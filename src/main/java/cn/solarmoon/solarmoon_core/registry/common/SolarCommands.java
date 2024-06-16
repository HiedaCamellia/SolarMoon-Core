package cn.solarmoon.solarmoon_core.registry.common;

import cn.solarmoon.solarmoon_core.api.entry.common.BaseCommandRegistry;
import cn.solarmoon.solarmoon_core.element.commond.GetAttributes;
import cn.solarmoon.solarmoon_core.element.commond.GetNBT;
import cn.solarmoon.solarmoon_core.element.commond.GetTag;

public class SolarCommands extends BaseCommandRegistry {
    @Override
    public void addRegistry() {
        add(new GetTag("getTag", 2, true));
        add(new GetNBT("getNBT", 2, true));
        add(new GetAttributes("getAttributes", 2, true));
    }
}
