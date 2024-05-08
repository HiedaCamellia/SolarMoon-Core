package cn.solarmoon.solarmoon_core.api.common.item.simple;

import cn.solarmoon.solarmoon_core.api.util.TextUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 具有食物属性的物品，会显示食物效果
 */
public class SimpleFoodItem extends Item {

    public SimpleFoodItem(int nutrition, float saturation) {
        super(new Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(nutrition).saturationMod(saturation).build()));
    }

    public SimpleFoodItem(int nutrition, float saturation, Supplier<MobEffectInstance> effectInstanceSupplier, float chance) {
        super(new Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(nutrition).saturationMod(saturation).effect(effectInstanceSupplier, chance).build()));
    }

    public SimpleFoodItem(FoodProperties foodProperties) {
        super(new Properties()
                .food(foodProperties));
    }

    public SimpleFoodItem(int nutrition, float saturation, int stacksTo) {
        super(new Properties().stacksTo(stacksTo)
                .food(new FoodProperties.Builder()
                        .nutrition(nutrition).saturationMod(saturation).build()));
    }

    public SimpleFoodItem(int nutrition, float saturation, int stacksTo, Supplier<MobEffectInstance> effectInstanceSupplier, float chance) {
        super(new Properties().stacksTo(stacksTo)
                .food(new FoodProperties.Builder()
                        .nutrition(nutrition).saturationMod(saturation).effect(effectInstanceSupplier, chance).build()));
    }

    public SimpleFoodItem(FoodProperties foodProperties, int stacksTo) {
        super(new Properties().stacksTo(stacksTo)
                .food(foodProperties));
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
            for (Pair<MobEffectInstance, Float> mobEffectInstanceFloatPair : effects) {
                var effect = mobEffectInstanceFloatPair.getFirst();
                Component base = TextUtil.getMinuteFormatEffectDuration(effect);
                components.add(base);
            }
        }
    }

}
