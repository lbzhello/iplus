package xyz.liujin.iplus.test.alibaba;

/**
 * 总共 n 瓶啤酒，3瓶啤酒可以换一瓶啤酒，7个瓶盖可以换一瓶啤酒，问总共可以喝几瓶啤酒？
 * @author liubaozhu lbzhello@qq.com
 * @since 2022/1/8
 */
public class Test {
    public static void main(String[] args) {
        int rst = total(13, 0, 0);
        System.out.println(rst);
    }

    static int total(int n, int p1, int g1) {
        // 瓶子
        int p = (n + p1)/3; // 第一轮换的瓶子 2
        int p2 = (n + p1)%3;  // 1

        // gaizi
        int g = (n + g1)/7; // 1
        int g2 = (n + g1)%7; // 0

        if (p > 0 || g > 0) {
            return n +  total(p + g, p2, g2);
        }

        return n;

    }
}
