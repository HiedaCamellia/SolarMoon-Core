package cn.solarmoon.solarmoon_core.api.commond;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

/**
 * 非常好基础指令，使我代码速度飞快
 */
public abstract class BaseCommand {

    protected LiteralArgumentBuilder<CommandSourceStack> builder;

    private final boolean enabled;

    /**
     * 基本的命令模版
     * @param head 头部命令行名称
     * @param permissionLevel 权限等级
     * @param enabled 是否启用
     */
    public BaseCommand(String head, int permissionLevel, boolean enabled) {
        this.builder = Commands.literal(head).requires(source -> source.hasPermission(permissionLevel));
        this.enabled = enabled;
        putExecution();
    }

    /**
     * 用于放入后续指令
     */
    public abstract void putExecution();

    /**
     * @return 返回最终的指令程序
     */
    public LiteralArgumentBuilder<CommandSourceStack> getBuilder() {
        return builder;
    }

    /**
     * 方便快速识别是否启用该命令
     */
    public boolean isEnabled() {
        return this.enabled;
    }

}
