package xyz.liujin.iplus.util.math;

public class MathUtil {
    /**
     * 随机生成证书，最大值 max
     * @param max
     * @return
     */
    public static int randomInt(int max) {
        return Double.valueOf(Math.random() * max).intValue();
    }
}
