package cn.solarmoon.solarmoon_core.api.network.serializer;


import cn.solarmoon.solarmoon_core.api.common.registry.NetPackEntry;
import cn.solarmoon.solarmoon_core.api.util.SerializeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public record ServerPackSerializer(String message, BlockPos pos, ItemStack stack, List<ItemStack> stacks, CompoundTag tag, FluidStack fluidStack, float f, int[] ints, List<Vec3> vec3List, String string) {

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(message);
        buf.writeBlockPos(Objects.requireNonNullElse(pos, BlockPos.ZERO));
        buf.writeItemStack(stack, true);
        SerializeHelper.writeItemStacks(buf, stacks);
        buf.writeNbt(tag);
        buf.writeFluidStack(fluidStack);
        buf.writeFloat(f);
        buf.writeVarIntArray(ints);
        SerializeHelper.writeVec3List(buf, vec3List);
        buf.writeUtf(string);
    }

    public static ServerPackSerializer decode(FriendlyByteBuf buf) {
        String message = buf.readUtf(32767);
        BlockPos pos = buf.readBlockPos();
        ItemStack stack = buf.readItem();
        List<ItemStack> stacks = SerializeHelper.readItemStacks(buf);
        CompoundTag tag = buf.readNbt();
        FluidStack fluidStack = buf.readFluidStack();
        float f = buf.readFloat();
        int[] ints = buf.readVarIntArray();
        List<Vec3> vec3List = SerializeHelper.readVec3List(buf);
        String string = buf.readUtf();
        return new ServerPackSerializer(message, pos, stack, stacks, tag, fluidStack, f, ints, vec3List, string);
    }

    public static void handle(ServerPackSerializer packet, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        for (NetPackEntry pack : NetPackEntry.ENTRIES) {
            for (var handler : pack.serverPackHandlers) {
                context.enqueueWork(() -> handler.doHandle(packet, context));
            }
        }
        supplier.get().setPacketHandled(true);
    }

}

