package com.demo.util;

import java.util.ArrayList;

/**
 * 为 {@code List<Integer>} 提供转换成 int[] 方法
 */
public class IntList extends ArrayList<Integer> {

    public int[] toIntArray() {
        return this.stream().mapToInt(Integer::intValue).toArray();
    }
}
