package com.mime.concurrent.executors;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @Author zhangjiaheng
 * @Description 自定义线程池
 **/
public class MyFixedThreadPool {

    private static final int WORKNUM = 5;

    private final int work_num;

    private final BlockingQueue<Runnable> queue;

    private final WorkThread[] workThread;

    MyFixedThreadPool() {
        this(WORKNUM);
    }

    MyFixedThreadPool(int work_num) {
        this.work_num = work_num;
        queue = new ArrayBlockingQueue<>(work_num);
        workThread = new WorkThread[work_num];
        for (int i = 0; i < work_num; i++) {
            workThread[i] = new WorkThread();
            workThread[i].start();
        }
    }

    public void execute(Runnable task) {
        try {
            queue.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void destory() {
        for (int i = 0; i < work_num; i++) {
            workThread[i].stopWorker();
            workThread[i] = null;
        }
        queue.clear();
    }

    /**
     * 从队列取出任务去执行
     */
    private class WorkThread extends Thread {
        @Override
        public void run() {
            Runnable r = null;
            try {
                while (!Thread.interrupted()) {
                    r = queue.take();
                    System.out.println("Task id[" + getId() + "] ready to execute");
                    r.run();
                }
            } catch (InterruptedException e) {
                System.out.println("线程中断");
            } finally {
                r = null;
            }
        }

        public void stopWorker() {
            interrupt();
        }
    }
}
