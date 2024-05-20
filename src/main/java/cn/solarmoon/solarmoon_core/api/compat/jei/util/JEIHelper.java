package cn.solarmoon.solarmoon_core.api.compat.jei.util;

import cn.solarmoon.solarmoon_core.SolarMoonCore;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.text.DecimalFormat;

public class JEIHelper {

    public static Component chanceText(float chance) {
        DecimalFormat df = new DecimalFormat("0.##");
        String result = df.format(chance * 100);
        return SolarMoonCore.TRANSLATOR.set("jei", "chance", ChatFormatting.GOLD, result);
    }

}
