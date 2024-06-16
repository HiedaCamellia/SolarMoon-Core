package cn.solarmoon.solarmoon_core.api.fluid_base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 由于大多数液体和水基本一致，所以搞个和水差不多的基本类
 */
public abstract class BaseFluid extends ForgeFlowingFluid {

    protected BaseFluid(Properties properties) {
        super(properties);
    }

    /**
     * 与水一致<br/>
     * 特指音效、粒子动画等
     */
    @Override
    public void animateTick(Level level, BlockPos pos, FluidState state, RandomSource randomSource) {
        if (!state.isSource() && !state.getValue(FALLING)) {
            if (randomSource.nextInt(64) == 0) {
                level.playLocalSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, randomSource.nextFloat() * 0.25F + 0.75F, randomSource.nextFloat() + 0.5F, false);
            }
        } else if (randomSource.nextInt(10) == 0) {
            level.addParticle(getUnderFluidParticle(), (double)pos.getX() + randomSource.nextDouble(), (double)pos.getY() + randomSource.nextDouble(), (double)pos.getZ() + randomSource.nextDouble(), 0.0D, 0.0D, 0.0D);
        }
    }

    public ParticleOptions getUnderFluidParticle() {return ParticleTypes.UNDERWATER;}

    /**
     * 液滴粒子
     */
    @Override
    protected ParticleOptions getDripParticle() {
        return null;
    }

    /**
     * 应用桶装音效
     */
    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Optional.of(SoundEvents.BUCKET_FILL);
    }

    /**
     * 不用管
     */
    @Override
    public boolean isSource(FluidState state) {
        return false;
    }

    /**
     * 不用管
     */
    @Override
    public int getAmount(FluidState state) {
        return 0;
    }

    /**
     * 液体方块
     */
    public abstract static class FluidBlock extends LiquidBlock {

        public FluidBlock(Supplier<? extends FlowingFluid> supplier) {
            super(supplier, BlockBehaviour.Properties.of()
                    //可替换液体为方块
                    .replaceable()
                    .noCollission()
                    .strength(100.0F)
                    //活塞推动摧毁
                    .pushReaction(PushReaction.DESTROY)
                    .noLootTable()
                    .liquid()
            );
        }

    }

    /**
     * 流动形式
     */
    public abstract static class Flowing extends BaseFluid {

        public Flowing(Properties properties) {
            super(properties);
            registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }

        public int getAmount(FluidState state) {
            return state.getValue(LEVEL);
        }

        public boolean isSource(FluidState state) {
            return false;
        }

    }

    /**
     * 液体源
     */
    public abstract static class Source extends BaseFluid {

        public Source(Properties properties) {
            super(properties);
        }

        public int getAmount(FluidState state) {
            return 8;
        }

        public boolean isSource(FluidState state) {
            return true;
        }

    }

    /**
     * 对应桶
     */
    public abstract static class Bucket extends BucketItem {

        public Bucket(Supplier<? extends Fluid> supplier) {
            super(supplier, new Item.Properties()
                    .craftRemainder(Items.BUCKET)
                    .stacksTo(1)
            );
        }

        @Override
        public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.CompoundTag nbt) {
            return new FluidBucketWrapper(stack);
        }

    }

}
