package xyz.liujin.iplus.util.debug;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 调试工具类
 */
public class DebugUtil {
    private static final Logger logger = LoggerFactory.getLogger(DebugUtil.class);

    /**
     * 线程等待一段时间再退出
     * @param millis
     */
    public static final void waitMillis(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            logger.error("failed to wait millis", e);
        }
    }
}
