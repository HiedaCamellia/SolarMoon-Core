package cn.solarmoon.solarmoon_core.registry.core;

import cn.solarmoon.solarmoon_core.network.NetworkPack;

import java.util.ArrayList;
import java.util.List;

/**
 * 基本网络数据包注册表
 */
public abstract class BasePackRegistry {

    private final List<NetworkPack> packs;

    public BasePackRegistry() {
        this.packs = new ArrayList<>();
    }

    /**
     * 使用packs来添加NetworkPack
     */
    public abstract void addRegistry();

    public void add(NetworkPack pack) {
        packs.add(pack);
    }

    public void register() {
        addRegistry();
        for (NetworkPack pack : packs) {
            pack.build();
        }
    }

}
