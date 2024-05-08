package cn.solarmoon.solarmoon_core.api.common.item.simple;

import cn.solarmoon.solarmoon_core.api.util.TextUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public class SimpleSeedFoodBlockItem extends ItemNameBlockItem {

    public SimpleSeedFoodBlockItem(Block block, FoodProperties foodProperties) {
        super(block, new Properties().food(foodProperties));
    }

    public SimpleSeedFoodBlockItem(Block block, int nutrition, float saturation, Supplier<MobEffectInstance> effectInstanceSupplier, float chance) {
        super(block, new Properties()
                .food(new FoodProperties.Builder()
                        .nutrition(nutrition).saturationMod(saturation).effect(effectInstanceSupplier, chance).build()));
    }

    public SimpleSeedFoodBlockItem(Block block, int nutrition, float saturation) {
        super(block, new Properties().food(new FoodProperties.Builder()
                .nutrition(nutrition).saturationMod(saturation).build()));
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
            for (int i = 0; i < effects.size(); i++) {
                var effect = effects.get(i).getFirst();
                Component base = TextUtil.getMinuteFormatEffectDuration(effect);
                components.add(base);
            }
        }
    }

}
