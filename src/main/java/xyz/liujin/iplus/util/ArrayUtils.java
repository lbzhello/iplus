package xyz.liujin.iplus.util;

import javax.annotation.Nullable;

public class ArrayUtils {
    /**
     * Determine whether the given array is empty:
     * i.e. {@code null} or of zero length.
     * @param array the array to check
     * @see ObjectUtils#isEmpty(Object)
     */
    public static boolean isEmpty(@Nullable Object[] array) {
        return (array == null || array.length == 0);
    }
}
