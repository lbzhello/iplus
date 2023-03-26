package com.demo.algorithm.sort;

import com.demo.util.LogUtil;

/**
 * 每次选择最小的元素放在最前面
 */
public class SelectionSort {
    public static void main(String[] args) {
        int[] arr = new int[] {5,3,8,2,9,1};
        selectionSort(arr);
        LogUtil.println(arr);
    }

    public static void selectionSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int min = i; // 最小值位置
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[min]) {
                    min = j;
                }
            }

            // 将最小值放入 i 位置
            int temp = arr[i];
            arr[i] = arr[min];
            arr[min] = temp;
        }
    }
}
