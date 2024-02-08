package cn.solarmoon.solarmoon_core.network;

import cn.solarmoon.solarmoon_core.network.serializer.ServerPackSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public interface IServerPackHandler {

    default void doHandle(ServerPackSerializer packet, NetworkEvent.Context context) {
        //快乐的定义时间
        ServerPlayer player = context.getSender();
        if (player == null) return;
        ServerLevel level = (ServerLevel) player.level();
        BlockPos pos = packet.pos();
        ItemStack stack = packet.stack();
        float f = packet.f();
        handle(player, level, pos, stack, f, packet.message());
    }

    /**
     * 对包进行处理，建议使用switch(message)进行识别
     */
    void handle(ServerPlayer player, ServerLevel level, BlockPos pos, ItemStack stack, float f, String message);

}
