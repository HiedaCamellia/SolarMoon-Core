package cn.solarmoon.solarmoon_core.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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

    public static String toMinuteFormat(int i) {
        int seconds = i;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static String toMinuteFormat(int i, boolean needBracket) {
        if (needBracket) return "(" + toMinuteFormat(i) + ")";
        else return toMinuteFormat(i);
    }

    /**
     * 提取tag中的词条（其实未必是tag中的词条，该方法只是能提取类似minecraft:这样和它冒号后的内容）
     * @param tag 要提取的tag
     * @param extractTag 指定一个词条
     * @return 返回词条对应的条目
     */
    public static String extract(String tag, String extractTag) {
        JsonObject jsonObject = JsonParser.parseString(tag).getAsJsonObject();
        return jsonObject.get(extractTag).getAsString();
    }
}
