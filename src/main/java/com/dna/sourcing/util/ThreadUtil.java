package com.dna.sourcing.util;

import java.util.concurrent.*;

public class ThreadUtil {

    private static int cpus = Runtime.getRuntime().availableProcessors();  // 核数

    private static ThreadFactory getMyThreadFactory(String name) {

        return new ThreadFactory() {
            //
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("");
                thread.setDaemon(true);  // TODO
                return thread;
            }
        };
    }

    // 单例
    private volatile static ThreadPoolExecutor uniqueInstance;

    public static ThreadPoolExecutor getInstance() {
        if (uniqueInstance == null) {
            synchronized (ThreadUtil.class) {
                if (uniqueInstance == null) {
                    //
                    int corePoolSize = 5 * cpus;
                    int maximumPoolSize = 15 * cpus;

                    // TODO：若设的值过大，当线程过多时，可能会撑爆内存
                    // TODO：若设的值过小，可能会被执行reject策略
                    int lbq_size = 1000;

                    // TODO 加队列

                    //
                    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(lbq_size));
                    threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
                    uniqueInstance = threadPoolExecutor;

                    //
                    return uniqueInstance;
                }
            }
        }
        return uniqueInstance;
    }
}
