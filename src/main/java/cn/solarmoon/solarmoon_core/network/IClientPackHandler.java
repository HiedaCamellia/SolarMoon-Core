package cn.solarmoon.solarmoon_core.network;

import cn.solarmoon.solarmoon_core.network.serializer.ClientPackSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

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
        float f = packet.f();
        int[] ints = packet.ints();
        String string = packet.string();
        //处理
        if (level == null || player == null) return;
        handle(player, level, pos, stack, tag, f, ints, string, stacks, packet.message());
    }

    /**
     * 对包进行处理，建议使用switch(message)进行识别
     */
    void handle(LocalPlayer player, ClientLevel level, BlockPos pos, ItemStack stack, CompoundTag tag, float f, int[] ints, String string, List<ItemStack> stacks, String message);

}
