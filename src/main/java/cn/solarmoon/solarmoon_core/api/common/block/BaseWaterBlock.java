package cn.solarmoon.solarmoon_core.api.common.block;


/**
 * 有基本的含水、朝向功能的纯方块<br/>
 */
public abstract class BaseWaterBlock extends BaseBlock implements IWaterLoggedBlock {

    public BaseWaterBlock(Properties properties) {
        super(properties);
    }

}
