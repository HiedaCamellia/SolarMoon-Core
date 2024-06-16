package cn.solarmoon.solarmoon_core.api.network;

import cn.solarmoon.solarmoon_core.api.data.SerializeHelper;
import cn.solarmoon.solarmoon_core.api.entry.common.NetPackEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;


public record ClientPackSerializer(String message, BlockPos pos, ItemStack stack, List<ItemStack> stacks,
                                   CompoundTag tag, FluidStack fluidStack, float f, int[] ints, List<Vec3> vec3List, boolean flag,
                                   int i,
                                   String string) {

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(message);
        buf.writeBlockPos(Objects.requireNonNullElse(pos, BlockPos.ZERO));
        SerializeHelper.writeItemStack(buf, stack);
        SerializeHelper.writeItemStacks(buf, stacks);
        buf.writeNbt(tag);
        buf.writeFluidStack(fluidStack);
        buf.writeFloat(f);
        buf.writeVarIntArray(ints);
        SerializeHelper.writeVec3List(buf, vec3List);
        buf.writeBoolean(flag);
        buf.writeInt(i);
        buf.writeUtf(string);
    }

    public static ClientPackSerializer decode(FriendlyByteBuf buf) {
        String message = buf.readUtf(32767);
        BlockPos pos = buf.readBlockPos();
        ItemStack stack = SerializeHelper.readItemStack(buf);
        List<ItemStack> stacks = SerializeHelper.readItemStacks(buf);
        CompoundTag tag = buf.readNbt();
        FluidStack fluidStack = buf.readFluidStack();
        float f = buf.readFloat();
        int[] ints = buf.readVarIntArray();
        List<Vec3> vec3List = SerializeHelper.readVec3List(buf);
        boolean flag = buf.readBoolean();
        int i = buf.readInt();
        String string = buf.readUtf();
        return new ClientPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, flag, i, string);
    }

    public static void handle(ClientPackSerializer packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        for (var pack : NetPackEntry.ENTRIES) {
            for (var handlers : pack.clientPackHandlers) {
                context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handlers.doHandle(packet)));
            }
        }
        supplier.get().setPacketHandled(true);
    }

}
