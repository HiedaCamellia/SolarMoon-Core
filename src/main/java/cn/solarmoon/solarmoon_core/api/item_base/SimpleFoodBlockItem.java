package cn.solarmoon.solarmoon_core.api.item_base;

import cn.solarmoon.solarmoon_core.api.util.TextUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 具有食物属性的可放置方块物品，会显示食物效果
 */
public class SimpleFoodBlockItem extends BlockItem {

    public SimpleFoodBlockItem(Block block, FoodProperties foodProperties) {
        super(block, new Properties().food(foodProperties));
    }

    public SimpleFoodBlockItem(Block block, int nutrition, float saturation, Supplier<MobEffectInstance> effectInstanceSupplier, float chance) {
        super(block, new Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(nutrition).saturationMod(saturation).effect(effectInstanceSupplier, chance).build()));
    }

    public SimpleFoodBlockItem(Block block, int nutrition, float saturation) {
        super(block, new Properties().food(new FoodProperties.Builder()
                .nutrition(nutrition).saturationMod(saturation).build()));
    }

    public SimpleFoodBlockItem(Block block, int stacksTo, int nutrition, float saturation) {
        super(block, new Properties().stacksTo(stacksTo).food(new FoodProperties.Builder()
                .nutrition(nutrition).saturationMod(saturation).build()));
    }

    public SimpleFoodBlockItem(Block block, int stacksTo, FoodProperties foodProperties) {
        super(block, new Properties().stacksTo(stacksTo).food(foodProperties));
    }

    public SimpleFoodBlockItem(Block block, int stacksTo, int nutrition, float saturation, Supplier<MobEffectInstance> effectInstanceSupplier, float chance) {
        super(block, new Properties().stacksTo(stacksTo)
                .food(new FoodProperties.Builder()
                        .nutrition(nutrition).saturationMod(saturation).effect(effectInstanceSupplier, chance).build()));
    }

    public boolean showEffectTooltip() {
        var effects = Objects.requireNonNull(getFoodProperties(getDefaultInstance(), null)).getEffects();
        return effects.size() != 1 || !effects.get(0).getFirst().getEffect().equals(MobEffects.HUNGER);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        super.appendHoverText(stack, level, components, flag);
        if (showEffectTooltip() && getFoodProperties(stack, null) != null) {
            var effects = Objects.requireNonNull(getFoodProperties(stack, null)).getEffects();
            List<MobEffectInstance> effectInstances = new ArrayList<>();
            for (Pair<MobEffectInstance, Float> mobEffectInstanceFloatPair : effects) {
                var effect = mobEffectInstanceFloatPair.getFirst();
                effectInstances.add(effect);
            }
            TextUtil.addPotionTooltipWithoutAttribute(effectInstances, components);
        }
    }

}
