package com.demo.algorithm.sort;

import com.demo.util.LogUtil;

public class QuickSort {

    public static void main(String[] args) {
        int[] arr = new int[] {5,3,8,2,9,1};
        quickSort(arr, 0, arr.length - 1);
        LogUtil.println(arr);
    }

    // 将数组分割成两部分分别进行排列
    public static void quickSort(int[] arr, int start, int end) {
        if (start >= end) {
            return;
        }
        int i = start;
        int j = end;
        int key = arr[i];
        while (i < j) {
            // 从右边找出第一个比 key 小的数
            while (i < j && arr[j] >= key) {
                j--;
            }

            // 右边存在比 key 小的数
            if (i < j) {
                arr[i] = arr[j];
                i++;
            }

            // 从左边找出第一个比 key 大的数
            while (i < j && arr[i] <= key) {
                i++;
            }

            // 左边存在比 key 大的数
            if (i < j) {
                arr[j] = arr[i];
                j--;
            }

        }
        arr[i] = key;

        quickSort(arr, start, i - 1);
        quickSort(arr, i + 1, end);
    }
}
