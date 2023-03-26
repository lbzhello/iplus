package com.demo.algorithm.sort;

import com.demo.util.LogUtil;

/**
 * 将未排序的元素插入已排序元素的合适位置
 */
public class InsertionSort {
    public static void main(String[] args) {
        int[] arr = new int[] {5,3,8,2,9,1};
        insertionSort(arr);
        LogUtil.println(arr);
    }

    public static void insertionSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) { // 已排序：0 ~ i；未排序 i ~ arr.len；左闭右开
            int cur = arr[i]; // 当前未排序的数
            int j = i - 1; // 倒叙比较
            while (j >= 0 && cur < arr[j]) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = cur;
        }
    }
}
