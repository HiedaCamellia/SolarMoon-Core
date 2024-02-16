package cn.solarmoon.solarmoon_core.registry.base;

import cn.solarmoon.solarmoon_core.registry.object.NetPackEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * 基本网络数据包注册表
 */
public abstract class BasePackRegistry {

    private final List<NetPackEntry> packs;

    public BasePackRegistry() {
        this.packs = new ArrayList<>();
    }

    /**
     * 使用packs来添加NetworkPack
     */
    public abstract void addRegistry();

    public void add(NetPackEntry pack) {
        packs.add(pack);
    }

    public void register() {
        addRegistry();
        for (NetPackEntry pack : packs) {
            pack.build();
        }
    }

}
