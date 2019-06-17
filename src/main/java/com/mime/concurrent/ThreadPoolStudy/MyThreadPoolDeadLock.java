package com.mime.concurrent.ThreadPoolStudy;

import java.util.concurrent.*;

/**
 * @Author zhangjiaheng
 * @Description 线程饥饿死锁演示示例
 **/
public class MyThreadPoolDeadLock {
    static ExecutorService singlePool = Executors.newSingleThreadExecutor();

    static class MyTask implements Callable<String> {
        String name;

        public MyTask(String name) {
            this.name = name;
        }

        @Override
        public String call() throws Exception {
            Future<String> inner = singlePool.submit(new MyTask("inner"));
            return inner.get();
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Future<String> result = singlePool.submit(new MyTask("outer"));
        System.out.println(result.get());
    }
}
