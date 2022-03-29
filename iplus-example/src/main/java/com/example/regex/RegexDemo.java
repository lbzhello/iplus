package com.example.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexDemo {
    // 匹配数字
    public static final String REG_NUMBER = "\\d+";

    /**
     * 全部匹配才返回 true
     * 1234 -> true
     * abc1234 -> false
     * @param text
     * @return
     */
    public boolean isNumber(String text) {
        return Pattern.matches(REG_NUMBER, text);
    }

    /**
     * text 存在匹配的子序列
     * 1234 -> true
     * abc123xyz -> true
     * abc -> false
     * @return
     */
    public boolean existNumber(String text) {
        // 编译, compile 变量可以多次使用
        Pattern compile = Pattern.compile(REG_NUMBER);
        // 获取 matter
        Matcher matcher = compile.matcher(text);

        // 替换第一个匹配的子序列，返回替换后的完整字符串
        String text2 = matcher.replaceFirst("hello");

        // 查找下一个匹配的子串
        while (matcher.find()) {
            // 返回匹配的字符序列
            String group = matcher.group();
            return true;
        }

        return false;
    }
}
