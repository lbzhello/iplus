package xyz.liujin.iplus.util;


import javax.annotation.Nullable;
import java.util.Collection;

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
}
