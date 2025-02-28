package cn.solarmoon.solarmoon_core.api.entry.common;

import cn.solarmoon.solarmoon_core.api.commond.BaseCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseCommandRegistry {

    protected final List<BaseCommand> commands = new ArrayList<>();

    public abstract void addRegistry();

    public void add(BaseCommand command) {
        commands.add(command);
    }

    public void onCommandsRegister(final RegisterCommandsEvent event) {
        addRegistry();
        for (var command : commands) {
            if (command.isEnabled()) {
                event.getDispatcher().register(command.getBuilder());
            }
        }
    }

    public void register() {
        MinecraftForge.EVENT_BUS.addListener(this::onCommandsRegister);
    }

}
