package cn.solarmoon.solarmoon_core.api.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IClientPackHandler {

    default void doHandle(ClientPackSerializer packet) {
        //快乐的定义时间
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        ClientLevel level = mc.level;
        BlockPos pos = packet.pos();
        ItemStack stack = packet.stack();
        List<ItemStack> stacks = packet.stacks();
        CompoundTag tag = packet.tag();
        FluidStack fluidStack = packet.fluidStack();
        float f = packet.f();
        int[] ints = packet.ints();
        List<Vec3> vec3List = packet.vec3List();
        boolean flag = packet.flag();
        int i = packet.i();
        String string = packet.string();
        //处理
        if (level == null || player == null) return;
        handle(player, level, pos, stack, tag, fluidStack, f, ints, string, stacks, vec3List, flag, i, packet.message());
    }

    /**
     * 对包进行处理，建议使用switch(message)进行识别
     */
    void handle(LocalPlayer player, ClientLevel level, BlockPos pos, ItemStack stack, CompoundTag tag, FluidStack fluidStack, float f, int[] ints, String string, List<ItemStack> stacks, List<Vec3> vec3List, boolean flag, int i, String message);

}
