package com.demo.algorithm.nowcoder.huawei;

import java.util.Scanner;

/**
 * 说给出一组整数，组合成最大数，数值可能很大
 * 比如 9，10 返回910
 */
public class Main3 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {// 注意，如果输入是多个测试用例，请通过while循环处理多个测试用例
            String line = in.nextLine();
            String[] numStrArr = line.split(" ");

            String maxNum = maxNum(numStrArr);
            System.out.println(maxNum);
        }
    }

    public static String maxNum(String[] numStrArr) {
        // 结果字符串拼接
        StringBuilder rstStr = new StringBuilder();

        for (int i = 0; i < numStrArr.length; i++) {
            rstStr.append(currentMaxNum(numStrArr, i));
        }
        return rstStr.toString();
    }

    // 获取第 i 个大的数，大的数冒泡到 i 位置
    // return 当前最大数
    public static String currentMaxNum(String[] numStrArr, int i) {
        for (int j = i + 1; j < numStrArr.length; j++) {
            // 若 j 比 i 大，则交换到 i 位置
            if (compare(numStrArr[i], numStrArr[j]) < 0) {
                String tmp = numStrArr[i];
                numStrArr[i] = numStrArr[j];
                numStrArr[j] = tmp;
            }
        }
        return numStrArr[i];
    }

    // 比较两个数大小
    public static int compare(String left, String right) {
        int llen = left.length(), rlen = right.length(), i = 0;
        for (; i < llen && i < rlen; i++) {
            // 字符串大小
            char lchar = left.charAt(i), rchar = right.charAt(i);
            if (lchar < rchar) {
                return -1;
            } else if (lchar > rchar) {
                return 1;
            }
        }

        /* 走到这里表示，同样长度情况下两个书相等 */

        if (llen == rlen) { // 相等
            return 0;
        } else if (llen > rlen) { // 左边的长，比较子串
            return compare(left.substring(i), right);
        } else { // 右边的长
            return compare(left, right.substring(i));
        }
    }
}
