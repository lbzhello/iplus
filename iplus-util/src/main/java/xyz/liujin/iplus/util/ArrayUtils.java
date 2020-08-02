package xyz.liujin.iplus.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ArrayUtils {
    /**
     * Return {@code true} if the supplied Array is {@code null} or empty.
     * Otherwise, return {@code false}.
     * @param array the Array to check
     * @return whether the given Array is empty
     * @see ObjectUtils#isEmpty(Object)
     */
    public static <E> boolean isEmpty(@Nullable E[] array) {
        return (array == null || array.length == 0);
    }

    /**
     * Return array consist of the given elements
     * @param elements
     * @param <E> the element type of the array
     * @return array consist of the given elements
     */
    public static <E> E[] of(@Nonnull E... elements) {
        return elements;
    }
}
