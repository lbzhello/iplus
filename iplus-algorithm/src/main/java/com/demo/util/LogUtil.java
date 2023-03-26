package com.demo.util;

public class LogUtil {
    public static void println(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i : arr) {
            sb.append(i).append(" ");
        }
        // 去掉末尾空格
        if (!sb.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        System.out.println(sb);
    }
}
