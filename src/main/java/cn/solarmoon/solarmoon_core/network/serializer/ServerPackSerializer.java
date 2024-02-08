package cn.solarmoon.solarmoon_core.network.serializer;


import cn.solarmoon.solarmoon_core.network.NetworkPack;
import cn.solarmoon.solarmoon_core.registry.Packs;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ServerPackSerializer(String message, BlockPos pos, ItemStack stack, float f) {

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(message);
        buf.writeBlockPos(pos);
        buf.writeItemStack(stack, true);
        buf.writeFloat(f);
    }

    public static ServerPackSerializer decode(FriendlyByteBuf buf) {
        String message = buf.readUtf(32767);
        BlockPos pos = buf.readBlockPos();
        ItemStack stack = buf.readItem();
        float f = buf.readFloat();
        return new ServerPackSerializer(message, pos, stack, f);
    }

    public static void handle(ServerPackSerializer packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        for (NetworkPack pack : Packs.get()) {
            for (var handler : pack.serverPackHandlers) {
                context.enqueueWork(() -> handler.doHandle(packet, context));
            }
        }
        supplier.get().setPacketHandled(true);
    }

}

