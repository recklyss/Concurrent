package com.mime.concurrent.CountDownLatchStudy;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author zhangjiaheng
 * @Description
 **/
public class CountDownLatchUtil {

    private CountDownLatch start;
    private CountDownLatch end;
    private int pollSize = 10;

    public CountDownLatchUtil() {
        this(10);
    }

    public CountDownLatchUtil(int pollSize) {
        this.pollSize = pollSize;
        start = new CountDownLatch(1);
        end = new CountDownLatch(pollSize);
    }

    public void latch(final MyFunctionInterface function) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(this.pollSize);
        for (int i = 0; i < this.pollSize; i++) {
            Runnable runnable = () -> {
                try {
                    // 这一行是为了保证所有线程都创建完成并提交到线程池
                    start.await();
                    function.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 线程创建之后 执行完毕之后进行countDown操作
                    end.countDown();
                }
            };
            executorService.submit(runnable);
        }
        // 保证所有线程都被提交到线程池开始执行
        start.countDown();
        // 在所有线程执行之前
        end.await();
        executorService.shutdown();
    }

}
