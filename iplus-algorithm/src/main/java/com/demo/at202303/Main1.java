package com.demo.at202303;

/**
 * 题目：给出一个二进制数组 data，你需要通过交换位置，将数组中 任何位置 上的 1 组合到一起，并返回所有可能中所需 最少的交换次数。
 * 示例 1：
 * 输入：[1,0,1,0,1]
 * 输出：1
 * 解释：
 * 有三种可能的方法可以把所有的 1 组合在一起：
 * [1,1,1,0,0]，交换 1 次；
 * [0,1,1,1,0]，交换 2 次；
 * [0,0,1,1,1]，交换 1 次。
 * 所以最少的交换次数为 1。
 *
 * 示例 2：
 * 输入：[0,0,0,1,0]
 * 输出：0
 * 解释：
 * 由于数组中只有一个 1，所以不需要交换。
 *
 * 示例 3：
 * 输入：[1,0,1,0,1,0,0,1,1,0,1]
 * 输出：3
 * 解释：
 * 交换 3 次，一种可行的只用 3 次交换的解决方案是 [0,0,0,0,0,1,1,1,1,1,1]。
 *
 * 滑动窗口
 */
public class Main1 {
    public static void main(String[] args) {
        int[] arr = new int[]{1,0,1,0,1,0,0,1,1,0,1};
        System.out.println(getMin(arr));
    }

    public static int getMin(int[] arr) {
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            int v = arr[i];
            if (v == 0) {
                continue;
            }
            int times = getTimes(arr, i);
            // 获取最小值
            if (min > times && times != 0) {
                min = times;
            }
        }
        return min;
    }

    // 以 index 位置向两边扩展
    public static int getTimes(int[] arr, int index) {
        int times = 0;
        int left = index - 1;

        // 从左边取 1 填充
        for (int i = 0; i < index; i++) {
            // 左边 1 和右边 0 交换
            if (i < left && arr[i] == 1) {
                times++;
                swap(arr, i, left);
            }
        }

        int right = index + 1;
        // 从右边 1 填充
        for (int i = arr.length - 1; i > index; i--) {
            // 右边边 1 和左边 0 交换
            if (i > right && arr[i] == 1) {
                times++;
                swap(arr, i, right);
            }
        }

        return times;
    }

    // 找出 index 左边 0
    public static void nextLeft(int[] arr, int index) {

    }

    // 找出右边 0
    public static void nextRight(int[] arr, int index) {

    }

    // 交换i1, i2索引位置
    public static void swap(int[] arr, int i1, int i2) {
        int tmp = arr[i1];
        arr[i1] = arr[i2];
        arr[i2] = tmp;
    }
}