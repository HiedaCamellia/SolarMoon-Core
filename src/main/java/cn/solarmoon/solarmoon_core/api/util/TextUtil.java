package cn.solarmoon.solarmoon_core.api.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;

import java.text.DecimalFormat;
import java.util.List;

public class TextUtil {

    public static String toRoman(int num) {
        String[] m = {"", "M", "MM", "MMM"};
        String[] c = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
        String[] x = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
        String[] i = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

        String thousands = m[num/1000];
        String hundreds = c[(num%1000)/100];
        String tens = x[(num%100)/10];
        String ones = i[num%10];

        return thousands + hundreds + tens + ones;
    }

    /**
     * @return 如果有小数就保留两位小数，没有就不写
     */
    public static String decimalRetentionOrNot(float f) {
        DecimalFormat df = new DecimalFormat("0.##");
        return df.format(f);
    }

    /**
     * 提取tag中的词条（其实未必是tag中的词条）<br/>
     * 如果 tag 是 {"name": "John"}，extractTag 是 "name"，那么这段代码将返回 "John"
     * @param tag 要提取的tag
     * @param extractTag 指定一个词条
     * @return 返回词条对应的条目
     */
    public static String extractTag(String tag, String extractTag) {
        JsonObject jsonObject = JsonParser.parseString(tag).getAsJsonObject();
        return jsonObject.get(extractTag).getAsString();
    }

    /**
     * 以分隔符为界限提取后面的内容<br/>
     * 如："minecraft:dirt", 分隔符输入":"，则能输出dirt
     * @param string 完整词条
     * @param separator 分隔符
     * @return 分隔符之后的内容
     */
    public static String extractString(String string, String separator) {
        String[] parts = string.split(separator);
        if (parts.length > 1) {
            return parts[1];
        }
        else return parts[0];
    }

    /**
     * @param idLike 形似minecraft:air的字符串
     * @return 以冒号为界分割字符串并转为标题形式，比如Minecraft Air。
     */
    public static String splitFromColon(String idLike) {
        String[] parts = idLike.split(":");
        return parts[0].substring(0, 1).toUpperCase() + parts[0].substring(1) + " " + parts[1].substring(0, 1).toUpperCase() + parts[1].substring(1);
    }

    /**
     * @return 添加如药水效果一样的工具提示（如：着火（00:10））
     */
    public static Component getPotionLikeTooltip(Component name, int tickDuration) {
        return Component.translatable(
                "potion.withDuration",
                name.copy(),
                StringUtil.formatTickDuration(tickDuration)).withStyle(ChatFormatting.BLUE);
    }

    /**
     * 同 {@link net.minecraft.world.item.alchemy.PotionUtils} 但是不会写出末尾的属性（如 生效后：抬升高度+1）
     */
    public static void addPotionTooltipWithoutAttribute(List<MobEffectInstance> effects, List<Component> components) {
        if (!effects.isEmpty()) {
            for(MobEffectInstance mobeffectinstance : effects) {
                MutableComponent mutablecomponent = Component.translatable(mobeffectinstance.getDescriptionId());
                MobEffect mobeffect = mobeffectinstance.getEffect();

                if (mobeffectinstance.getAmplifier() > 0) {
                    mutablecomponent = Component.translatable("potion.withAmplifier", mutablecomponent, Component.translatable("potion.potency." + mobeffectinstance.getAmplifier()));
                }

                if (!mobeffectinstance.endsWithin(20)) {
                    mutablecomponent = Component.translatable("potion.withDuration", mutablecomponent, MobEffectUtil.formatDuration(mobeffectinstance, 1));
                }

                components.add(mutablecomponent.withStyle(mobeffect.getCategory().getTooltipFormatting()));
            }
        }

    }

}
