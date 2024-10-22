package cn.solarmoon.solarmoon_core.feature;

import net.minecraftforge.fml.loading.FMLEnvironment;


public abstract class SolarMoonBase {

    public SolarMoonBase() {
        if (FMLEnvironment.dist.isClient()) {
            objectsClientOnly();
            eventObjectsClientOnly();
        }
        objects();
        eventObjects();
        xData();
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
     * 联动实例
     */
    public abstract void compats();

}
