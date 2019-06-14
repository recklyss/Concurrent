package com.mime.concurrent.executors;

import java.util.Random;

/**
 * @Author zhangjiaheng
 * @Description 测试线程池
 **/
public class TestMyThreadPool {

    public static void main(String[] args) {
        MyFixedThreadPool pool = new MyFixedThreadPool();
        pool.execute(new MyTask("testA"));
        pool.execute(new MyTask("testB"));
        pool.execute(new MyTask("testC"));
        pool.execute(new MyTask("testD"));
        pool.execute(new MyTask("testE"));
        pool.execute(new MyTask("testF"));
        pool.execute(new MyTask("testG"));
        System.out.println(pool);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pool.destory();
        System.out.println(pool);
    }

    public static class MyTask implements Runnable {
        private String name;
        private Random r = new Random();

        MyTask(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(r.nextInt(1000) + 1000);
                System.out.println("任务： " + name + " --- 执行完成");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
