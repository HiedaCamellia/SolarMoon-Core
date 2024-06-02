package cn.solarmoon.solarmoon_core.api.network;

import cn.solarmoon.solarmoon_core.api.network.serializer.ServerPackSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;

public interface IServerPackHandler {

    default void doHandle(ServerPackSerializer packet, NetworkEvent.Context context) {
        //快乐的定义时间
        ServerPlayer player = context.getSender();
        if (player == null) return;
        ServerLevel level = (ServerLevel) player.level();
        BlockPos pos = packet.pos();
        ItemStack stack = packet.stack();
        List<ItemStack> stacks = packet.stacks();
        CompoundTag tag = packet.tag();
        FluidStack fluidStack = packet.fluidStack();
        float f = packet.f();
        int[] ints = packet.ints();
        List<Vec3> vec3List = packet.vec3List();
        boolean flag = packet.flag();
        String string = packet.string();
        //处理
        handle(player, level, pos, stack, tag, fluidStack, f, ints, string, stacks, vec3List, flag, packet.message());
    }

    /**
     * 对包进行处理，建议使用switch(message)进行识别
     */
    void handle(ServerPlayer player, ServerLevel level, BlockPos pos, ItemStack stack, CompoundTag tag, FluidStack fluidStack, float f, int[] ints, String string, List<ItemStack> stacks, List<Vec3> vec3List, boolean flag, String message);

}
