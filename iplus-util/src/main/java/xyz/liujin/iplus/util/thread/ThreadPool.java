package xyz.liujin.iplus.util.thread;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * 项目通用线程池
 */
public class ThreadPool implements Executor {

    private static class Singleton {
        private static final ExecutorService INSTANCE = ThreadPoolBuilder.builder().build();
    }

    /**
     * 获取默认的线程池
     * @return
     */
    public static ExecutorService getDefault() {
        return Singleton.INSTANCE;
    }

    /**
     * 创建新的线程池
     * @return
     */
    public static ThreadPoolBuilder builder() {
        return ThreadPoolBuilder.builder();
    }

    @Override
    public void execute(Runnable command) {
        getDefault().execute(command);
    }

}
