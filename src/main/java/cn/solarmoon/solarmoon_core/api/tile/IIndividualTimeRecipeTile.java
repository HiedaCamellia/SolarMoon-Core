package cn.solarmoon.solarmoon_core.api.tile;

import net.minecraft.world.item.crafting.Recipe;

import java.util.Optional;

/**
 * 同一个方块中需要处理多个独立配方时间的接口（如营火），自动实现配方时间的存储
 */
public interface IIndividualTimeRecipeTile<R extends Recipe<?>> {

    String SINGLE_STACK_TIME = "IndividualTime";

    int[] getTimes();

    /**
     * 一般是记录配方所需时间（最大时间），一般是渲染用
     */
    int[] getRecipeTimes();

    /**
     * 由于数组的特殊性，一般get后直接修改即可，set除了同步一般不用
     */
    void setTimes(int[] ints);

    void setRecipeTimes(int[] ints);

    /**
     * @return 第index栏位的匹配配方
     */
    Optional<R> getCheckedRecipe(int index);

}
