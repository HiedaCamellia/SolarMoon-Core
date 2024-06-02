package cn.solarmoon.solarmoon_core.core.common.registry;

import cn.solarmoon.solarmoon_core.api.common.registry.BaseCommandRegistry;
import cn.solarmoon.solarmoon_core.core.common.command.GetAttributes;
import cn.solarmoon.solarmoon_core.core.common.command.GetNBT;
import cn.solarmoon.solarmoon_core.core.common.command.GetTag;

public class SolarCommands extends BaseCommandRegistry {
    @Override
    public void addRegistry() {
        add(new GetTag("getTag", 2, true));
        add(new GetNBT("getNBT", 2, true));
        add(new GetAttributes("getAttributes", 2, true));
    }
}
