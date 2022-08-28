package com.demo.algorithm.nowcoder.stringlength;

import java.util.Scanner;

/**
 * 描述
 * 计算字符串最后一个单词的长度，单词以空格隔开，字符串长度小于5000。（注：字符串末尾不以空格为结尾）
 * 输入描述：
 * 输入一行，代表要计算的字符串，非空，长度小于5000。
 *
 * 输出描述：
 * 输出一个整数，表示输入字符串最后一个单词的长度。
 *
 * 示例1
 *  输入：hello nowcoder
 *  输出：8
 *  说明：最后一个单词为nowcoder，长度为8
 */
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) { // 注意 while 处理多个 case
            String line = in.nextLine();
            int len = lastStrLen(line);
            System.out.println(len);
        }
    }

    public static int lastStrLen(String text) {
        if (text == null || text.length() == 0) {
            return 0;
        }

        // 去掉结尾空白字符
        text = text.trim();

        int len = 0;
        for (int i = text.length() - 1; i >= 0; i--) {
            char c = text.charAt(i);
            // 判断是空白字符
            if (Character.isWhitespace(c)) {
                return len;
            } else {
                len++;
            }
        }
        return len;
    }
}
