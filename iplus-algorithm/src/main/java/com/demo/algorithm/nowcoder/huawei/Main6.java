package com.demo.algorithm.nowcoder.huawei;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 现在你总共有 n 门课需要选，记为 0 到 n-1。
 * 在选修某些课程之前需要一些先修课程。 例如，想要学习课程 0 ，你需要先完成课程 1 ，我们用一个匹配来表示他们: [0,1]
 * 给定课程总量以及它们的先决条件，返回你为了学完所有课程所安排的学习顺序。
 * 可能会有多个正确的顺序，你只要返回一种就可以了。如果不可能完成所有课程，返回一个空数组。
 * 示例 1:
 * 输入: 2, [[1,0]]
 * 输出: [0,1]
 * 解释: 总共有 2 门课程。要学习课程 1，你需要先完成课程 0。因此，正确的课程顺序为 [0,1]。
 * 示例 2:
 * 输入: 4, [[1,0],[2,0],[3,1],[3,2]]
 * 输出: [0,1,2,3] or [0,2,1,3]
 * 解释: 总共有 4 门课程。要学习课程 3，你应该先完成课程 1 和课程 2。并且课程 1 和课程 2 都应该排在课程 0 之后。
 *      因此，一个正确的课程顺序是 [0,1,2,3] 。另一个正确的排序是 [0,2,1,3] 。
 */
public class Main6 {
    public static void main(String[] args) {
        int[][] arr = new int[][]{
                new int[]{1, 0},
                new int[]{2, 0},
                new int[]{3, 1},
                new int[]{3, 2}
        };
        List<Integer> rst = select(4, arr);
        System.out.println(rst);

    }

    public static final List<Integer> select(int n, int[][] arr2) {
        List<Integer> rst = new ArrayList<>();
        if (arr2 == null || arr2.length == 0) {
            return Collections.emptyList();
        }

        for (int i = 0; i < arr2.length; i++) {
            boolean b = addArr(rst, arr2[i]);
            // 不符合条件
            if (!b) {
                return null;
            }
        }

        return rst;
    }

    // 新增课程列表
    public static final boolean addArr(List<Integer> rst, int[] arr) {
        // 当前课程位置，下一个课程位置必须大于等于此
        int pos = 0;
        // 每个课程顺序
        for (int i = arr.length - 1; i >= 0; i--) {
            int elem = arr[i];
            int newPos = addElem(rst, elem);
            if (newPos < pos) {
                return false;
            } else {
                pos = newPos;
            }
        }
        return true;
    }

    // 新增一门课程
    // return 插入位置
    public static int addElem(List<Integer> rst, int elem) {
        for (int i = 0; i < rst.size(); i++) {
            if (elem == rst.get(i)) {
                return i;
            }
        }
        // 如果没有相同的课程则放入最后
        rst.add(elem);
        return rst.size();
    }
}
