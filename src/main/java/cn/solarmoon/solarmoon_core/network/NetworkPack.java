package cn.solarmoon.solarmoon_core.network;

import cn.solarmoon.solarmoon_core.network.serializer.ClientPackSerializer;
import cn.solarmoon.solarmoon_core.network.serializer.ServerPackSerializer;
import cn.solarmoon.solarmoon_core.registry.Packs;
import cn.solarmoon.solarmoon_core.util.NetworkSender;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NetworkPack {

    public List<IClientPackHandler> clientPackHandlers;
    public List<IServerPackHandler> serverPackHandlers;

    private int packetID = 0;
    private final SimpleChannel network;
    private final ResourceLocation name;
    private final Side side;

    /**
     * 简单的网络数据包
     * @param name 包id
     * @param side 逻辑侧，CLIENT -> 客户端，SERVER -> 服务端
     */
    public NetworkPack(ResourceLocation name, Side side) {
        clientPackHandlers = new ArrayList<>();
        serverPackHandlers = new ArrayList<>();
        this.name = name;
        this.side = side;
        network = NetworkRegistry.ChannelBuilder.named(name)
                .networkProtocolVersion(() -> "1")
                .clientAcceptedVersions(string -> true)
                .serverAcceptedVersions(string -> true)
                .simpleChannel();
    }

    private int id() {
        return packetID++;
    }

    public SimpleChannel get() {
        return network;
    }

    public Side getSide() {
        return side;
    }

    public NetworkSender getSender() {
        return new NetworkSender(this);
    }

    public NetworkPack addHandler(IClientPackHandler... handlers) {
        if (side == Side.CLIENT) Collections.addAll(clientPackHandlers, handlers);
        return this;
    }

    public NetworkPack addHandler(IServerPackHandler... handlers) {
        if (side == Side.SERVER) Collections.addAll(serverPackHandlers, handlers);
        return this;
    }

    public void build() {
        if (side == Side.CLIENT) {
            network.messageBuilder(ClientPackSerializer.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                    .decoder(ClientPackSerializer::decode)
                    .encoder(ClientPackSerializer::encode)
                    .consumerMainThread(ClientPackSerializer::handle)
                    .add();
        }
        if (side == Side.SERVER) {
            network.messageBuilder(ServerPackSerializer.class, id(), NetworkDirection.PLAY_TO_SERVER)
                    .decoder(ServerPackSerializer::decode)
                    .encoder(ServerPackSerializer::encode)
                    .consumerMainThread(ServerPackSerializer::handle)
                    .add();
        }
        Packs.put(name, this);
    }

    public enum Side {
        CLIENT,
        SERVER
    }

}
