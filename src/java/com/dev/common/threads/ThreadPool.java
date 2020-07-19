package com.dev.common.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {

    private static ExecutorService pool;

    public static ExecutorService getThreadPool() {

        pool = Executors.newFixedThreadPool((Runtime.getRuntime().availableProcessors()) * 2);//(10);

        return pool;
    }
}
