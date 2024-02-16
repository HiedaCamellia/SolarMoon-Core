package cn.solarmoon.solarmoon_core.util.static_utor;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;

import javax.annotation.Nullable;

/**
 * 使用public static来固定在mod中
 */
public class Debug {

    private final String prefix;
    private final ForgeConfigSpec.ConfigValue<Boolean> debugConfig;

    public Debug(String prefix, ForgeConfigSpec.ConfigValue<Boolean> debugConfig) {
        this.prefix = prefix;
        this.debugConfig = debugConfig;
    }

    /**
     * 自动从客户端侧发送debug
     */
    public void send(String string, @Nullable Level level) {
        Minecraft mc = Minecraft.getInstance();
        if(level != null) {
            if(!level.isClientSide) return;
        }
        if(mc.player == null || !debugConfig.get()) return;
        mc.player.sendSystemMessage(Component.literal(prefix  + string));
    }

    /**
     * 客户端侧debug
     */
    public void send(String string) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null && debugConfig.get()) {
            mc.player.sendSystemMessage(Component.literal(prefix  + string));
        }
    }

    /**
     * 服务端测debug
     */
    public void send(String string, Player player) {
        Level level = player.level();
        if(!level.isClientSide && debugConfig.get()) {
            player.sendSystemMessage(Component.literal(prefix  + string));
        }
    }

}
