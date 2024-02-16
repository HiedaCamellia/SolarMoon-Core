package cn.solarmoon.solarmoon_core.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.DecimalFormat;

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

    /**
     * @return 如果有小数就保留两位小数，没有就不写
     */
    public static String decimalRetentionOrNot(float f) {
        DecimalFormat df = new DecimalFormat("0.##");
        return df.format(f);
    }

    public static String toMinuteFormat(int i, boolean needBracket) {
        if (needBracket) return "(" + toMinuteFormat(i) + ")";
        else return toMinuteFormat(i);
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
        return parts[1];
    }

}
