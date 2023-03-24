package com.demo.at202303;

/**
 * 给定一个含有 n 个正整数的数组和一个正整数 s ，
 * 找出该数组中满足其和 ≥ s 的长度最小的连续子数组。如果不存在符合条件的连续子数组，返回 0。
 *
 * 示例: 
 *
 * 输入: s = 7, nums = [2,3,1,2,4,3]
 * 输出: 2
 * 解释: 子数组 [4,3] 是该条件下的长度最小的连续子数组。
 */
public class Main2 {
    public static void main(String[] args) {

        int[] arr = new int[]{1,1,1,1,1,1,5};
        int f = findMinLen(arr, 4);
        System.out.println(f);
    }

    // 查找最小子串
    public static int findMinLen(int[] arr, int s) {
        if (arr == null || arr.length == 0) {
            return 0;
        }

        if (arr.length == 1) {
            return arr[0] >= s ? 1 : 0;
        }

        int rst = Integer.MAX_VALUE;

        for (int i = 1; i < arr.length; i++) {
            int min = getMin(arr, i, s);
            if (min != 0 && min < rst) {
                rst = min;
            }
        }

        return rst == Integer.MAX_VALUE ? 0 : rst;
    }

    // 查找 index 位置往前，累加值大于等于 s 的最小长度
    public static int getMin(int[] arr, int index, int s) {
        int acc = arr[index]; // 向前累加，大于 s 返回

        // 考虑当前值大于 s 的情况
        if (acc >= s) {
            return 1;
        }

        int times = 1;
        for (int i = index; i > 0; i--) {
            acc = acc + arr[i - 1];

            times++; // 累加一次

            if (acc >= s) {
                return times;
            }
        }
        return 0;
    }
}
