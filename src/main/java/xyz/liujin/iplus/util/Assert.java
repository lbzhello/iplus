package xyz.liujin.iplus.util;

import javax.validation.constraints.NotNull;

public class Assert {
    public static void notNull(@NotNull Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
