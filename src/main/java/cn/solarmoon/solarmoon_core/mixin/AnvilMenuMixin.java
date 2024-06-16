package cn.solarmoon.solarmoon_core.mixin;

import cn.solarmoon.solarmoon_core.api.util.AttributeUtil;
import cn.solarmoon.solarmoon_core.feature.embedding.AttributeForgingRecipe;
import cn.solarmoon.solarmoon_core.feature.embedding.EmbeddingData;
import cn.solarmoon.solarmoon_core.feature.embedding.EmbeddingUtil;
import cn.solarmoon.solarmoon_core.registry.common.SolarCapabilities;
import cn.solarmoon.solarmoon_core.registry.common.SolarRecipes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {

    @Shadow @Final private DataSlot cost;

    @Shadow public int repairItemCountCost;

    @Shadow protected abstract boolean mayPickup(Player p_39023_, boolean p_39024_);

    public AnvilMenuMixin(@Nullable MenuType<?> p_39773_, int p_39774_, Inventory p_39775_, ContainerLevelAccess p_39776_) {
        super(p_39773_, p_39774_, p_39775_, p_39776_);
        EmbeddingUtil.AnvilMenuModifyCheck();
    }

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    public void addResult(CallbackInfo ci) {
        ItemStack input = inputSlots.getItem(0);
        if (getCheckedRecipe().isPresent()) {
            ItemStack result = input.copy();
            AttributeForgingRecipe recipe = getCheckedRecipe().get();
            AttributeUtil.addAttributeToStack(result, recipe);
            EmbeddingData resultEmbedData = result.getCapability(SolarCapabilities.ITEMSTACK_DATA).orElse(null).getEmbeddingData();
            resultEmbedData.embedItem(recipe.material(), false);
            cost.set(recipe.expCost());
            resultSlots.setItem(2, result);
            ci.cancel();
        }
    }

    @Inject(method = "mayPickup", at = @At(value = "HEAD"), cancellable = true)
    public void ifPickup(Player player, boolean flag, CallbackInfoReturnable<Boolean> cir) {
        if (validRecipeInput() && player.experienceLevel > cost.get()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "onTake", at = @At("HEAD"))
    public void onTake(Player player, ItemStack result, CallbackInfo ci) {
        if (getCheckedRecipe().isPresent()) {
            repairItemCountCost = getCheckedRecipe().get().material().getCount();
        }
    }

    private boolean validRecipeInput() {
        return getCheckedRecipe().isPresent();
    }

    private Optional<AttributeForgingRecipe> getCheckedRecipe() {
        ItemStack input = inputSlots.getItem(0);
        ItemStack consume = inputSlots.getItem(1);
        Level level = player.level();
        List<AttributeForgingRecipe> recipes = level.getRecipeManager().getAllRecipesFor(SolarRecipes.ATTRIBUTE_FORGING.get());
        EmbeddingData inputEmbeddingData = input.getCapability(SolarCapabilities.ITEMSTACK_DATA).orElse(null).getEmbeddingData();
        return recipes.stream().filter(r -> r.input().test(input)
                        && r.isMaterialSufficient(consume)
                        && inputEmbeddingData.getItemCount(r.material(), false) < r.maxForgeCount()).findFirst();
        //这里有三个条件：1.输入物匹配 2.镶嵌物匹配且数量足够 3.输入物已镶嵌的该镶嵌物数量小于最大镶嵌数
    }

}
