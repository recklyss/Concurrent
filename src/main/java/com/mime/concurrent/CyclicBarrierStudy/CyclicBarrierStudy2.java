package com.mime.concurrent.CyclicBarrierStudy;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

public class CyclicBarrierStudy2 {
  public static final AtomicInteger count = new AtomicInteger();
  public static final int NUM = 10;

  static class lastTask implements Runnable {
    @Override
    public void run() {
      System.out.println("我是最后要做的事情，睡两秒");
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("我睡醒了，结束了");
    }
  }

  static class MyThread extends Thread {
    private CyclicBarrier cb;

    public MyThread(String name, CyclicBarrier cb) {
      super(name);
      this.cb = cb;
    }

    @Override
    public void run() {
      try {
        System.out.println(this.getName() + "到达，等待其他线程到达");
        cb.await();
      } catch (Exception e) {
        e.printStackTrace();
      }
      System.out.println(this.getName() + "获取到了数字：" + getNext());
    }
  }

  public static void main(String[] args) {
    CyclicBarrier cb = new CyclicBarrier(NUM, new lastTask());
    for (int i = 0; i < NUM; i++) {
      new MyThread("线程" + i, cb).start();
    }
  }

  private static int getNext() {
    return count.incrementAndGet();
  }
}
