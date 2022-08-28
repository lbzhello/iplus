package com.demo.algorithm.nowcoder.huawei;

import java.util.Scanner;

/**
 * 说给出一组整数，组合成最大数，数值可能很大
 * 比如 9，10 返回910
 */
public class Main4 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextInt()) {// 注意，如果输入是多个测试用例，请通过while循环处理多个测试用例
            long num1 = in.nextLong();
            long num2 = in.nextLong();
            long val = compositeMax(num1, num2);
            System.out.println(val);
        }
    }

    public static long compositeMax(long num1, long num2) {
        String n1Str = String.valueOf(num1);
        String n2Str = String.valueOf(num2);
        int n1len = n1Str.length(), n2len = n2Str.length();
        // 第一个放大倍数
        int mul1 = 1;
        for (int i = 0; i < n1len; i++) {
            mul1 = mul1 * 10;
        }
        long val1 = num1 * mul1 + num2;

        // 第二个数放大倍数
        int mul2 = 1;
        for (int i = 0; i < n1len; i++) {
            mul2 = mul2 * 10;
        }
        long val2 = num2 * mul2 + num1;

        return Math.max(val1, val2);
    }
}
