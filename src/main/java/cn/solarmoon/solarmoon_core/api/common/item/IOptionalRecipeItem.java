package cn.solarmoon.solarmoon_core.api.common.item;

import cn.solarmoon.solarmoon_core.api.common.capability.IItemStackData;
import cn.solarmoon.solarmoon_core.api.common.capability.serializable.itemstack.RecipeSelectorData;
import cn.solarmoon.solarmoon_core.api.util.HitResultUtil;
import cn.solarmoon.solarmoon_core.core.common.registry.SolarCapabilities;
import cn.solarmoon.solarmoon_core.api.util.CapabilityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * 手持实现该接口物品时，对目视方块进行配方匹配检测。<br/>
 */
public interface IOptionalRecipeItem<T extends Recipe<RecipeWrapper>> {

    /**
     * 设置（获取）与之绑定的配方
     */
    RecipeType<T> getRecipeType();

    /**
     * 是此接口运作的基础，所有的get都要在此为true时才会返回非空值
     * @param hitState 目视方块的blockState
     * @return 配方如何与目视方块匹配
     */
    boolean recipeMatches(T recipe, BlockState hitState, Level level, BlockHitResult hitResult, Player player);

    /**
     * @return 自定的展示在gui选框上的物品集合
     */
    List<ItemStack> getItemsOnGui(Player player);

    /**
     * @return 默认输出在匹配配方中具体选择的那个配方
     */
    default Optional<T> getSelectedRecipe(ItemStack stack, Player player) {
        if (!getMatchingRecipes(player).isEmpty()) {
            return Optional.ofNullable(getMatchingRecipes(player).get(getHitBlockRecipeIndex(stack, player)));
        }
        return Optional.empty();
    }

    /**
     * 根据玩家目视方块的匹配逻辑来获取对应的匹配配方的集合
     */
    default List<T> getMatchingRecipes(Player player) {
        Level level = player.level();
        BlockHitResult hit = HitResultUtil.getPlayerPOVHitResult(player.level(), player, ClipContext.Fluid.NONE);
        BlockPos pos = hit.getBlockPos();
        BlockState hitState = level.getBlockState(pos);
        List<T> recipes = level.getRecipeManager().getAllRecipesFor(this.getRecipeType());
        return recipes.stream()
                .filter((recipe) -> this.recipeMatches(recipe, hitState, level, hit, player))
                .sorted(Comparator.comparingInt(Object::hashCode)) //必须进行排序，否则会导致gui的列表和此列表顺序不一致
                .toList();
    }

    /**
     * @return 玩家目视的方块
     */
    default Block getHitBlock(Player player) {
        Level level = player.level();
        BlockHitResult hit = HitResultUtil.getPlayerPOVHitResult(player.level(), player, ClipContext.Fluid.NONE);
        BlockPos pos = hit.getBlockPos();
        BlockState hitState = level.getBlockState(pos);
        return hitState.getBlock();
    }

    /**
     * @return 对应item的配方选值
     */
    default int getHitBlockRecipeIndex(ItemStack stack, Player player) {
        Block block = getHitBlock(player);
        IItemStackData itemStackData = CapabilityUtil.getData(stack, SolarCapabilities.ITEMSTACK_DATA);
        RecipeSelectorData selector = itemStackData.getRecipeSelectorData();
        if (!getMatchingRecipes(player).isEmpty()) {
            return selector.getIndex(block);
        }
        return 0;
    }

}
