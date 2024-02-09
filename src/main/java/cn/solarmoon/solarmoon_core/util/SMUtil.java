package cn.solarmoon.solarmoon_core.util;

import cn.solarmoon.solarmoon_core.registry.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class SMUtil {

    /**
     * 自动从客户端侧发送debug
     */
    public static void deBug(String string, @Nullable Level level) {
        Minecraft mc = Minecraft.getInstance();
        if(level != null) {
            if(!level.isClientSide) return;
        }
        if(mc.player == null || !Config.deBug.get()) return;
        mc.player.sendSystemMessage(Component.literal("[§6SM§f] "  + string));
    }

    /**
     * 客户端侧debug
     */
    public static void deBug(String string) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.sendSystemMessage(Component.literal("[§6SM§f] "  + string));
        }
    }

    /**
     * 服务端测debug
     */
    public static void deBug(String string, Player player) {
        Level level = player.level();
        if(!level.isClientSide) {
            player.sendSystemMessage(Component.literal("[§6SM§f] "  + string));
        }
    }

}
