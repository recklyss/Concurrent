package com.mime.concurrent.ThreadLocalStudy;

/**
 * @Author zhangjiaheng
 * @Description 线程本地副本学习
 **/
public class ThreadLocalStudy {
    private ThreadLocal<Integer> count;

    public ThreadLocalStudy(int count) {
        this.count = new ThreadLocal<>();
        this.count.set(count);
    }

    public int getNext() {
        this.count.set(this.count.get() + 1);
        return this.count.get();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(new myRunnable()).start();
        }
    }
}


class myRunnable implements Runnable {

    @Override
    public void run() {
        System.out.println(new ThreadLocalStudy(100).getNext());
    }
}
