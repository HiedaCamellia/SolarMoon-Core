package cn.solarmoon.solarmoon_core.api.common.block_entity.iutor;

/**
 * 一般用于动画渲染的接口<br/>
 * ticks会从0tick到100，然后循环<br/>
 * 使用setChanged可以把tick归0<br/>
 * 接入者需要实现private int ticks以传入
 */
@Deprecated
public interface IBlockEntityAnimateTicker {

    int getTicks();

    void setTicks(int ticks);

    /**
     * @return 以防万一的记录值，对两个tick过程的标识应该有作用
     */
    default float getLast() {return 0;};

    default void setLast(float last) {}

}
