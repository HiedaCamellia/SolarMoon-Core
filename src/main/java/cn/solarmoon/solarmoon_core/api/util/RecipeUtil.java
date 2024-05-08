package cn.solarmoon.solarmoon_core.api.util;

import cn.solarmoon.solarmoon_core.api.common.recipe.serializable.ChanceResult;
import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecipeUtil {

    /**
     * @return 获取所有可能的输出物品
     */
    public static List<ItemStack> getResults(NonNullList<ChanceResult> chanceResults) {
        return chanceResults.stream()
                .map(ChanceResult::stack)
                .collect(Collectors.toList());
    }

    /**
     * 根据幸运等级对results进行随机选取并输出最终结果
     */
    public static List<ItemStack> getRolledResults(Player player, NonNullList<ChanceResult> chanceResults) {
        int fortuneLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.BLOCK_FORTUNE, player.getItemInHand(InteractionHand.MAIN_HAND));
        MobEffectInstance luckEffect = player.getEffect(MobEffects.LUCK);
        int luckPotionLevel = (luckEffect != null) ? luckEffect.getAmplifier() + 1 : 0;
        RandomSource rand = player.getRandom();
        int luck = fortuneLevel + luckPotionLevel;
        List<ItemStack> results = new ArrayList<>();
        for (ChanceResult output : chanceResults) {
            ItemStack stack = output.rollOutput(rand, luck);
            if (!stack.isEmpty()) {
                results.add(stack);
            }
        }
        return results;
    }

}
