package cn.solarmoon.solarmoon_core.common.item;

import cn.solarmoon.solarmoon_core.common.capability.IPlayerData;
import cn.solarmoon.solarmoon_core.common.capability.serializable.RecipeSelectorData;
import cn.solarmoon.solarmoon_core.registry.SolarCapabilities;
import cn.solarmoon.solarmoon_core.util.CapabilityUtil;
import cn.solarmoon.solarmoon_core.util.RecipeUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * 手持实现该接口物品时，对目视方块进行配方匹配检测。<br/>
 * 一般情况下为了实现完整效果，需要对具体物品搭建桥梁：<br/>
 * recipeMatches<br/>
 * matchingRecipes<br/>
 * optionalRecipes<br/>
 */
public interface IOptionalRecipeItem<T extends Recipe<RecipeWrapper>> {

    /**
     * 设置（获取）与之绑定的配方
     */
    RecipeType<T> getRecipeType();

    /**
     * 检查目视方块是否与绑定配方匹配方法
     * @param recipe 绑定配方
     * @param hitStack 目视方块的物品形式（也就是cloneItemStack）
     * @return 是否与配方匹配
     */
    boolean recipeCheckAndUpdate(T recipe, ItemStack hitStack, Level level, BlockHitResult hitResult, Player player);

    /**
     * @return 目视方块是否匹配配方（默认已设置）
     */
    boolean recipeMatches();

    /**
     * 设置recipeMatches的值
     */
    void setRecipeMatch(boolean recipeMatches);

    /**
     * 注意，某些情况下会返回null，而且某些情况往往难以预料，使用前务必检查！
     * @return 默认输出在匹配配方中具体选择的那个配方
     */
    @Nullable
    default T getSelectedRecipe(Entity entity) {
        if (entity instanceof Player player) {
            IPlayerData playerData = CapabilityUtil.getData(player, SolarCapabilities.PLAYER_DATA);
            RecipeSelectorData selector = playerData.getRecipeSelectorData();
            if (getMatchingRecipes() != null && !getMatchingRecipes().isEmpty()) {
                return getMatchingRecipes().get(selector.getRecipeIndex(getRecipeType()));
            }
        }
        return null;
    }

    /**
     * @return 返回对应一个输入的所有可能输出的配方（可选配方），没有匹配的返回空列表
     */
    @Nullable
    List<T> getMatchingRecipes();

    /**
     * 添加匹配的配方到可选配方列表中
     */
    default void addMatchingRecipe(T recipe) {
        getMatchingRecipes().add(recipe);
    }

    /**
     * 一般用于渲染，当然也可用于别的地方<br/>
     * 使用前最好检查是否为null（这玩意竟然能是null我是没想到的）
     * @return 建议返回所有匹配配方的所有可选的具体输出物品，具体逻辑请自定义。
     */
    @Nullable
    default List<ItemStack> getOptionalOutputs() {return new ArrayList<>();}

    /**
     * 默认获取可选输出物的具体选择物（需先实现getOptionalOutputs）<br/>
     * 注意，某些情况下会返回null，而且某些情况往往难以预料，使用前务必检查！
     */
    @Nullable
    default ItemStack getSelectedOutput(Entity entity) {
        if (entity instanceof Player player) {
            IPlayerData playerData = CapabilityUtil.getData(player, SolarCapabilities.PLAYER_DATA);
            RecipeSelectorData selector = playerData.getRecipeSelectorData();
            if (getOptionalOutputs() != null && !getOptionalOutputs().isEmpty()) {
                return getOptionalOutputs().get(selector.getIndex(getRecipeType()));
            }
        }
        return null;
    }

    /**
     * 物品栏中tick，用以时刻调用配方检测，当然也可单独在某些特定位置更新配方<br/>
     * 显然这是每个stack单独调用的（见mixin），因此只要是其中的方法，不必担心不独立的问题
     */
    default void recipeCheckAndUpdate(Player player, Level level, BlockHitResult hit) {
        BlockPos pos = hit.getBlockPos();
        ItemStack hitStack = level.getBlockState(pos).getCloneItemStack(hit, level, pos, player);
        List<T> recipes = RecipeUtil.getRecipes(level, getRecipeType());
        //此处需重置为false以防一直为true
        //List同理
        setRecipeMatch(false);
        getMatchingRecipes().clear();
        for (T recipe : recipes) {
            //配方匹配就加入到匹配合集中
            if (recipeCheckAndUpdate(recipe, hitStack, level, hit, player)) {
                addMatchingRecipe(recipe);
            }
            //一旦有一个配方匹配，就固定recipeMatches为true
            //逻辑是设置匹配结果直到true为止
            if (!recipeMatches()) {
                setRecipeMatch(recipeCheckAndUpdate(recipe, hitStack, level, hit, player));
            }
        }
    }

}
