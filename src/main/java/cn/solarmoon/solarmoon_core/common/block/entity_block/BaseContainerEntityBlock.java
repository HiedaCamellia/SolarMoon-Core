package cn.solarmoon.solarmoon_core.common.block.entity_block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * 便捷的基本容器的实体方块<br/>
 * 有很多和I*BlockEntity对应的实用方法<br/>
 * 当然这个类只属于标识或是过渡类，本身方法都是从接口和basic中来的，因此不是必须继承
 */
public abstract class BaseContainerEntityBlock extends BasicEntityBlock implements IContainerEntityBlock {

    public BaseContainerEntityBlock(Properties properties) {
        super(properties);
    }

    /**
     * 强制继承类实现use，因为容器一般需要特殊功能
     */
    @Override
    public abstract InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult);

}
