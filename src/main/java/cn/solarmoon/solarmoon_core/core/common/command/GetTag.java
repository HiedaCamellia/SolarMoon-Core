package cn.solarmoon.solarmoon_core.core.common.command;

import cn.solarmoon.solarmoon_core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.api.common.command.BaseCommand;
import com.mojang.brigadier.Command;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GetTag extends BaseCommand {

    /**
     * 基本的命令模版
     *
     * @param head            头部命令行名称
     * @param permissionLevel 权限等级
     * @param enabled         是否启用
     */
    public GetTag(String head, int permissionLevel, boolean enabled) {
        super(head, permissionLevel, enabled);
    }

    @Override
    public void putExecution() {
        builder.executes(context -> getTag(context.getSource().getPlayerOrException()));
    }

    public int getTag(ServerPlayer player) {
        ItemStack stack = player.getMainHandItem();
        var tags = stack.getTags().collect(Collectors.toSet());
        List<String> strings = new ArrayList<>();
        tags.forEach(tag -> strings.add(tag.location().toString()));
        player.sendSystemMessage(Component.literal(strings.toString()));
        return Command.SINGLE_SUCCESS;
    }

}
