package cn.solarmoon.solarmoon_core.core.common.command;

import cn.solarmoon.solarmoon_core.SolarMoonCore;
import cn.solarmoon.solarmoon_core.api.common.command.BaseCommand;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collection;

public class GetAttributes extends BaseCommand {

    /**
     * 基本的命令模版
     *
     * @param head            头部命令行名称
     * @param permissionLevel 权限等级
     * @param enabled         是否启用
     */
    public GetAttributes(String head, int permissionLevel, boolean enabled) {
        super(head, permissionLevel, enabled);
    }

    @Override
    public void putExecution() {
        builder.then(
                Commands.argument("LivingEntity", EntityArgument.entities())
                .executes(e -> getAttributes(EntityArgument.getEntities(e, "LivingEntity"), e.getSource().getPlayer()))
        );
    }

    public int getAttributes(Collection<? extends Entity> entities, ServerPlayer player) {
        entities.forEach(entity -> {
            if (entity instanceof LivingEntity living) {
                player.sendSystemMessage(Component.literal(living.getAttributes().save().toString()));
            }
        });
        return Command.SINGLE_SUCCESS;
    }

}
