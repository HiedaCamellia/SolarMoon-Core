package cn.solarmoon.solarmoon_core.util;

import cn.solarmoon.solarmoon_core.network.serializer.ClientPackSerializer;
import cn.solarmoon.solarmoon_core.network.serializer.ServerPackSerializer;
import cn.solarmoon.solarmoon_core.registry.object.NetPackEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.ArrayList;
import java.util.List;

public class NetworkSender {

    private final SimpleChannel channel;
    private final NetPackEntry.Side side;

    private final BlockPos pos = new BlockPos(0, 0, 0);
    private final List<ItemStack> stacks = new ArrayList<>();
    private final ItemStack stack = ItemStack.EMPTY;
    private final CompoundTag tag = new CompoundTag();
    private final float f = 0;
    private final String string = "";

    public NetworkSender(NetPackEntry pack) {
        channel = pack.get();
        side = pack.getSide();
    }

    public void send(String message) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stacks, tag, f, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, f));
        }
    }

    public void send(String message, BlockPos pos) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stacks, tag, f, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, f));
        }
    }

    public void send(String message, float f) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stacks, tag, f, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, f));
        }
    }

    public void send(String message, float f, String string) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stacks, tag, f, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, f));
        }
    }

    public void send(String message, float f, ItemStack stack) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stacks, tag, f, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, f));
        }
    }

    public void send(String message, BlockPos pos, List<ItemStack> stacks) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stacks, tag, f, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, f));
        }
    }

    public void send(String message, BlockPos pos, CompoundTag tag) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stacks, tag, f, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, f));
        }
    }

}
