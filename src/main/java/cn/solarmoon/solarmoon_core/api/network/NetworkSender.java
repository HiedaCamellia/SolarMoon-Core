package cn.solarmoon.solarmoon_core.api.network;

import cn.solarmoon.solarmoon_core.api.network.serializer.ServerPackSerializer;
import cn.solarmoon.solarmoon_core.api.network.serializer.ClientPackSerializer;
import cn.solarmoon.solarmoon_core.api.common.registry.NetPackEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
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
    private final FluidStack fluidStack = FluidStack.EMPTY;
    private final float f = 0;
    private final int[] ints = new int[0];
    private final List<Vec3> vec3List = new ArrayList<>();
    private final String string = "";

    public NetworkSender(NetPackEntry pack) {
        channel = pack.get();
        side = pack.getSide();
    }

    public void send(String message) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
    }

    public void send(String message, BlockPos pos) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
    }

    public void send(String message, List<Vec3> vec3List) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
    }

    public void send(String message, float f) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
    }

    public void send(String message, ItemStack stack) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
    }

    public void send(String message, String string) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
    }

    public void send(String message, float f, String string) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
    }

    public void send(String message, float f, ItemStack stack) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
    }

    public void send(String message, ItemStack stack, CompoundTag tag) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
    }

    public void send(String message, FluidStack fluidStack, List<Vec3> vec3List) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
    }

    public void send(String message, List<Vec3> vec3List, ItemStack stack) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
    }

    public void send(String message, BlockPos pos, int[] ints) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
    }

    public void send(String message, BlockPos pos, List<ItemStack> stacks) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
    }

    public void send(String message, BlockPos pos, CompoundTag tag) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
    }

    public void send(String message, BlockPos pos, float f) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
    }

    public void send(String message, BlockPos pos, ItemStack stack) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
    }

    public void send(String message, BlockPos pos, ItemStack stack, float f) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
    }

    public void send(String message, BlockPos pos, List<ItemStack> stacks, List<Vec3> vec3List) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string));
        }
    }

}
