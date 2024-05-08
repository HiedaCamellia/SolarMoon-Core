package cn.solarmoon.solarmoon_core.api.common.registry;

/**
 * 快捷实现初始化注册，使用enum进行单例固定
 */
public interface IRegister {

    default void register() {}

}
