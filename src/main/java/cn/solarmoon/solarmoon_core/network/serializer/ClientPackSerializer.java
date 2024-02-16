package cn.solarmoon.solarmoon_core.network.serializer;

import cn.solarmoon.solarmoon_core.registry.object.NetPackEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;


public record ClientPackSerializer(String message, BlockPos pos, List<ItemStack> stacks, CompoundTag tag, float f, String string) {

    public void encode(FriendlyByteBuf buf) {
        buf.writeUtf(message);
        buf.writeBlockPos(Objects.requireNonNullElse(pos, BlockPos.ZERO));

        buf.writeInt(stacks.size());
        for (ItemStack stack : stacks) {
            buf.writeItem(stack);
        }

        buf.writeNbt(tag);
        buf.writeFloat(f);
        buf.writeUtf(string);
    }

    public static ClientPackSerializer decode(FriendlyByteBuf buf) {
        String message = buf.readUtf(32767);
        BlockPos pos = buf.readBlockPos();

        int size = buf.readInt();
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            stacks.add(buf.readItem());
        }

        CompoundTag tag = buf.readNbt();
        float f = buf.readFloat();
        String string = buf.readUtf();
        return new ClientPackSerializer(message, pos, stacks, tag, f, string);
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
