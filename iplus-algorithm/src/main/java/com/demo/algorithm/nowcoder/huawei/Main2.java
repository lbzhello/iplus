package com.demo.algorithm.nowcoder.huawei;

import java.util.Scanner;

/**
 * 有一组数，初始步数可选 n (1 <= n < 数组长度的一半)，然后以当前值为步数继续向下走，找出最小几次可以走完，无法走完返回 -1
 * 比如 5 6 5 1 2 8 6 9, 初始步数可选 2，跳到 5，然后走 5 步走完，所以返回 2（假设这个是最小次数）；
 */
public class Main2 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNextLine()) {// 注意，如果输入是多个测试用例，请通过while循环处理多个测试用例
            String line = in.nextLine();
            String[] numStrArr = line.split(" ");
            int[] numArr = new int[numStrArr.length];
            for (int i = 0; i < numStrArr.length; i++) {
                numArr[i] = Integer.parseInt(numStrArr[i]);
            }
            int step = minStep(numArr);
            System.out.println(step);
        }
    }

    public static int minStep(int[] arr) {
        if (arr.length == 1) {
            return 1;
        }
        int len = arr.length,  minStep = len;
        for (int pos = 1; pos < len / 2; pos++) {
            int curStep = nextStep(arr, 1, pos);
            if (curStep != -1 && curStep < minStep) {
                minStep = curStep;
            }
        }

        if (minStep != len) {
            return minStep;
        } else {
            return -1;
        }
    }

    // 走下一步
    public static int nextStep(int[] arr, int step, int pos) {
        int len = arr.length;
        if (pos == len - 1) {
            return step;
        } else if (pos > len - 1) {
            return -1;
        } else {
            pos = pos + arr[pos];
            step++;
            return nextStep(arr, step, pos);
        }
    }
}
