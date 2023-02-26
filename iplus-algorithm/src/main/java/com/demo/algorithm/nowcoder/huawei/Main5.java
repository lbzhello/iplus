package com.demo.algorithm.nowcoder.huawei;

import java.util.HashSet;
import java.util.Set;

/**
 * 给一整数，整数每一位的平方相加结果如果为 1 则是快乐数；
 * 如果不为 1 则继续将每一位相加，知道是快乐书，或者无限循环；
 * 例1：19
 * 1^2 + 9^2 = 82
 * 8^2 + 2^2 = 68
 * 6^2 + 8^2 = 100
 * 1^2 + 0^2 + 0^2 = 1
 * 所以 19 是快乐数
 * 例2：
 * 输入：2
 * 输出：false
 */
public class Main5 {
    public static void main(String[] args) {
        System.out.println(checkHappyNum(19));
    }

    // 所有数集合
    public static final Set<Long> allNum = new HashSet<>();
    private static final boolean checkHappyNum(long num) {
        long nextNum = nextHappyNum(num);
        while (true) {
            if (isHappyNum(nextNum)) {
                return true;
            } else if (allNum.contains(nextNum)) {
                return false;
            } else {
                allNum.add(nextNum);
                nextNum = nextHappyNum(nextNum);
            }
        }
    }

    // 判断当前数是否快乐数
    private static final boolean isHappyNum(long num) {
        return num == 1;
    }

    // 获取下一个数
    private static final long nextHappyNum(long num) {
        long rst = 0;
        String numStr = String.valueOf(num);
        // 每位数相加
        for (int i = 0; i < numStr.length(); i++) {
            long curPositionNum = Long.parseLong(String.valueOf(numStr.charAt(i)));
            long curPositionNum2 = curPositionNum * curPositionNum;
            rst += curPositionNum2;
        }
        return rst;
    }
}
