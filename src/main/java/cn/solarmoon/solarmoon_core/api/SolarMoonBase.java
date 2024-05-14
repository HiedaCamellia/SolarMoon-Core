package cn.solarmoon.solarmoon_core.api;

import net.minecraftforge.fml.loading.FMLEnvironment;


public abstract class SolarMoonBase {

    public SolarMoonBase() {
        if (FMLEnvironment.dist.isClient()) {
            objectsClientOnly();
            eventObjectsClientOnly();
            abilitiesClientOnly();
        }
        objects();
        eventObjects();
        xData();
        abilities();
        compats();
    }

    /**
     * 原版各项必须只在客户端的实例
     */
    public abstract void objectsClientOnly();

    /**
     * 原版各项实例
     */
    public abstract void objects();

    /**
     * 基于forge事件的必须只在客户端的实例
     */
    public abstract void eventObjectsClientOnly();

    /**
     * 基于forge事件的实例
     */
    public abstract void eventObjects();

    /**
     * 外部数据
     */
    public abstract void xData();

    /**
     * 自定特殊能力但是只在客户端
     */
    public abstract void abilitiesClientOnly();

    /**
     * 自定特殊能力
     */
    public abstract void abilities();

    /**
     * 联动实例
     */
    public abstract void compats();

}
