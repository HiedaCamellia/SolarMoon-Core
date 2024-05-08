package cn.solarmoon.solarmoon_core.api.util;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;

public class PlayerUtil {

    /**
     * 快速让玩家吃下FoodProperties的所有效果<br/>
     * 同时还有音效
     */
    public static void eat(Player player, FoodProperties foodProperties) {
        player.getFoodData().eat(foodProperties.getNutrition(), foodProperties.getSaturationModifier());
        var effects = foodProperties.getEffects();
        for (var effectP : effects) {
            RandomSource random = player.getRandom();
            if (random.nextFloat() <= effectP.getSecond()) {
                player.addEffect(effectP.getFirst());
            }
        }
        player.level().playSound(player, player.getOnPos().above(), SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

}
