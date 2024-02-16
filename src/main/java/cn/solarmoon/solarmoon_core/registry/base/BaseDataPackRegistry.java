package cn.solarmoon.solarmoon_core.registry.base;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseDataPackRegistry {

    private final List<PreparableReloadListener> dataPacks;

    public BaseDataPackRegistry() {
        this.dataPacks = new ArrayList<>();
    }

    /**
     * 使用add来添加数据包
     */
    public abstract void addRegistry();

    public void add(PreparableReloadListener data) {
        dataPacks.add(data);
    }

    @SubscribeEvent
    public void onAddReloadListener(AddReloadListenerEvent event) {
        for (var data : dataPacks) {
            event.addListener(data);
        }
    }

    public void register() {
        addRegistry();
        MinecraftForge.EVENT_BUS.register(this);
    }

}
