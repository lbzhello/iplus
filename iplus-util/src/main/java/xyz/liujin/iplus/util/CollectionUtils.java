package xyz.liujin.iplus.util;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CollectionUtils {
    /**
     * Return {@code true} if the supplied Collection is {@code null} or empty.
     * Otherwise, return {@code false}.
     * @param collection the Collection to check
     * @return whether the given Collection is empty
     */
    public static boolean isEmpty(@Nullable Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    /**
     * Return {@code true} if the supplied Array is {@code null} or empty.
     * Otherwise, return {@code false}.
     * @param array the Array to check
     * @return whether the given Array is empty
     */
    public static <E> boolean isEmpty(@Nullable  E[] array) {
        return ArrayUtils.isEmpty(array);
    }

    @SuppressWarnings("varargs")
    public static <E> List<E> listOf(@Nonnull E... elements) {
        List<E> list = new ArrayList<>(elements.length);
        for (E e : elements) {
            list.add(e);
        }
        return list;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(ArrayUtils.of(1, 2, 3, 4)));
    }

}
