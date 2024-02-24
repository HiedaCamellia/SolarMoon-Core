package cn.solarmoon.solarmoon_core.common.block;

import net.minecraft.world.level.block.state.properties.IntegerProperty;

/**
 * 一个基于物品栈属性的接口，本身只是注册一个属性，什么也不做
 */
public interface IStackBlock {

    IntegerProperty STACK = IntegerProperty.create("stack", 1, 64);

}
