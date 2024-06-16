package cn.solarmoon.solarmoon_core.element.commond;

import cn.solarmoon.solarmoon_core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.api.commond.BaseCommand;
import com.mojang.brigadier.Command;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class GetNBT extends BaseCommand {

    /**
     * 基本的命令模版
     *
     * @param head            头部命令行名称
     * @param permissionLevel 权限等级
     * @param enabled         是否启用
     */
    public GetNBT(String head, int permissionLevel, boolean enabled) {
        super(head, permissionLevel, enabled);
    }

    @Override
    public void putExecution() {
        builder.executes(context -> getTag(context.getSource().getPlayerOrException()));
    }

    public int getTag(ServerPlayer player) {
        ItemStack stack = player.getMainHandItem();
        var tag = stack.getTag();
        if (tag != null) {
            player.sendSystemMessage(Component.literal(tag.getAsString()));
        } else player.sendSystemMessage(SolarMoonCore.TRANSLATOR.set("command", "get_nbt.null"));;
        return Command.SINGLE_SUCCESS;
    }

}
