package cn.solarmoon.solarmoon_core.api.network;

import cn.solarmoon.solarmoon_core.api.entry.common.NetPackEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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

    private BlockPos pos = new BlockPos(0, 0, 0);
    private List<ItemStack> stacks = new ArrayList<>();
    private ItemStack stack = ItemStack.EMPTY;
    private CompoundTag tag = new CompoundTag();
    private FluidStack fluidStack = FluidStack.EMPTY;
    private float f = 0;
    private int[] ints = new int[0];
    private List<Vec3> vec3List = new ArrayList<>();
    private boolean flag = false;
    private int i = 0;
    private String string = "";

    public NetworkSender(NetPackEntry pack) {
        channel = pack.get();
        side = pack.getSide();
    }

    public NetworkSender pos(BlockPos pos) {
        this.pos = pos;
        return this;
    }

    public NetworkSender stacks(List<ItemStack> stacks) {
        this.stacks = stacks;
        return this;
    }

    public NetworkSender stack(ItemStack stack) {
        this.stack = stack;
        return this;
    }

    public NetworkSender tag(CompoundTag tag) {
        this.tag = tag;
        return this;
    }

    public NetworkSender fluidStack(FluidStack fluidStack) {
        this.fluidStack = fluidStack;
        return this;
    }

    public NetworkSender f(float f) {
        this.f = f;
        return this;
    }

    public NetworkSender ints(int[] ints) {
        this.ints = ints;
        return this;
    }

    public NetworkSender vec3List(List<Vec3> vec3List) {
        this.vec3List = vec3List;
        return this;
    }

    public NetworkSender flag(boolean flag) {
        this.flag = flag;
        return this;
    }

    public NetworkSender i(int i) {
        this.i = i;
        return this;
    }

    public NetworkSender string(String string) {
        this.string = string;
        return this;
    }

    public void send(String message) {
        if (side == NetPackEntry.Side.CLIENT) {
            channel.send(PacketDistributor.ALL.noArg(), new ClientPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, flag, i, string));
        }
        if (side == NetPackEntry.Side.SERVER) {
            channel.sendToServer(new ServerPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, flag, i, string));
        }
    }

}
