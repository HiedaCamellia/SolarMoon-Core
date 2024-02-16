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
 * 默认情况下会自动同步客户端各种容器的信息、设置红石信号逻辑、-中键物品、打落物品会存有给类容器信息、放置方块会读取stack的信息设置各类容器信息
 */
public abstract class BaseTCEntityBlock extends BaseTankEntityBlock implements IContainerEntityBlock {

    public BaseTCEntityBlock(Properties properties) {
        super(properties);
    }

    /**
     * 强制继承类实现use，因为容器一般需要特殊功能
     */
    @Override
    public abstract InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult);

}
