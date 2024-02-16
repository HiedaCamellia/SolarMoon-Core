package cn.solarmoon.solarmoon_core.registry.object;

import cn.solarmoon.solarmoon_core.network.IClientPackHandler;
import cn.solarmoon.solarmoon_core.network.IServerPackHandler;
import cn.solarmoon.solarmoon_core.network.serializer.ClientPackSerializer;
import cn.solarmoon.solarmoon_core.network.serializer.ServerPackSerializer;
import cn.solarmoon.solarmoon_core.util.NetworkSender;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NetPackEntry {

    public List<IClientPackHandler> clientPackHandlers;
    public List<IServerPackHandler> serverPackHandlers;

    private final String modId;

    private int packetID = 0;
    private SimpleChannel network;
    private Side side;

    public static final List<NetPackEntry> ENTRIES = new ArrayList<>();

    public NetPackEntry(String modId) {
        clientPackHandlers = new ArrayList<>();
        serverPackHandlers = new ArrayList<>();
        this.modId = modId;
    }

    public NetPackEntry id(String id) {
        ResourceLocation name = new ResourceLocation(modId, id);
        network = NetworkRegistry.ChannelBuilder.named(name)
                .networkProtocolVersion(() -> "1")
                .clientAcceptedVersions(string -> true)
                .serverAcceptedVersions(string -> true)
                .simpleChannel();
        return this;
    }

    public NetPackEntry side(Side side) {
        this.side = side;
        return this;
    }

    public NetPackEntry addHandler(IClientPackHandler... handlers) {
        if (side == Side.CLIENT) Collections.addAll(clientPackHandlers, handlers);
        return this;
    }

    public NetPackEntry addHandler(IServerPackHandler... handlers) {
        if (side == Side.SERVER) Collections.addAll(serverPackHandlers, handlers);
        return this;
    }

    public NetPackEntry build() {
        if (side == Side.CLIENT) {
            network.messageBuilder(ClientPackSerializer.class, packId(), NetworkDirection.PLAY_TO_CLIENT)
                    .decoder(ClientPackSerializer::decode)
                    .encoder(ClientPackSerializer::encode)
                    .consumerMainThread(ClientPackSerializer::handle)
                    .add();
        }
        if (side == Side.SERVER) {
            network.messageBuilder(ServerPackSerializer.class, packId(), NetworkDirection.PLAY_TO_SERVER)
                    .decoder(ServerPackSerializer::decode)
                    .encoder(ServerPackSerializer::encode)
                    .consumerMainThread(ServerPackSerializer::handle)
                    .add();
        }
        ENTRIES.add(this);
        return this;
    }

    private int packId() {
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

    public enum Side {
        CLIENT,
        SERVER
    }

}
