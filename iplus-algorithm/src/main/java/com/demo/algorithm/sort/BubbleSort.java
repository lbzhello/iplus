package com.demo.algorithm.sort;

import com.demo.util.LogUtil;

/**
 * 冒泡排序算法是把较小的元素往前调或者把较大的元素往后调。
 * 这种方法主要是通过对相邻两个元素进行大小的比较，
 * 根据比较结果和算法规则对该二元素的位置进行交换，这样逐个依次进行比较和交换，就能达到排序目的。
 * 冒泡排序的基本思想是，首先将第1个和第2个记录的关键字比较大小，如果是逆序的，就将这两个记录进行交换，
 * 再对第2个和第3个记录的关键字进行比较，
 * 依次类推，重复进行上述计算，直至完成第(n一1)个和第n个记录的关键字之间的比较，
 * 此后，再按照上述过程进行第2次、第3次排序，直至整个序列有序为止。
 * 排序过程中要特别注意的是，当相邻两个元素大小一致时，这一步操作就不需要交换位置，
 * 因此也说明冒泡排序是一种严格的稳定排序算法，它不改变序列中相同元素之间的相对位置关系。
 */
public class BubbleSort {
    public static void main(String[] args) {
        int[] arr = new int[] {5,3,8,2,9,1};
        bubbleSort(arr);
        LogUtil.println(arr);
    }

    public static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[i]) {
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }
}
