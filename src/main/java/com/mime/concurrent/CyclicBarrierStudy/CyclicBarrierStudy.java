package com.mime.concurrent.CyclicBarrierStudy;

import cn.hutool.core.date.DateUtil;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierStudy {

  public static final int NUM = 20;

  static CyclicBarrier cyclicBarrier = new CyclicBarrier(NUM + 1);

  static class MyRun implements Runnable {
    @Override
    public void run() {
      try {
        System.out.println(Thread.currentThread().getName() + " 随即休眠10秒以内");
        Thread.sleep(new Random().nextInt(10000));
        System.out.println("时间：" + DateUtil.now() + " " + Thread.currentThread().getId() + "已经到达");
        cyclicBarrier.await();
        System.out.println("时间：" + DateUtil.now() + " " + "所有线程都到达，出发！");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {
    ExecutorService s = Executors.newCachedThreadPool();
    for (int i = 0; i < NUM; i++) {
      s.submit(new MyRun());
    }
    try {
      Thread.sleep(30000);
      System.out.println("主线程休眠30秒");
      cyclicBarrier.await();
      System.out.println(cyclicBarrier.getNumberWaiting());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
