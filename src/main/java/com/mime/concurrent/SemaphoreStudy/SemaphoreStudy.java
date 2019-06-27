package com.mime.concurrent.SemaphoreStudy;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author zhangjiaheng @Description 使用信号量做一个限流的功能 信号量的作用：设置资源可用数量， 线程将资源使用完之后无法再使用，
 * 可以做等待其他资源的释放后获取到之后再使用
 */
public class SemaphoreStudy {

  public static final int N = 100;

  public static final int M = 1000;

  private static final AtomicInteger F = new AtomicInteger();

  private static final AtomicInteger S = new AtomicInteger();

  private static Semaphore store = new Semaphore(N);

  public static void main(String[] args) throws BrokenBarrierException, InterruptedException {

    CyclicBarrier BARRIER = new CyclicBarrier(M + 1);

    ExecutorService pool = Executors.newCachedThreadPool();

    for (int i = 0; i < M; i++) {
      pool.execute(
          () -> {
            try {
              BARRIER.await();
            } catch (Exception e) {
              e.printStackTrace();
            }
            getData();
          });
    }
    System.out.println("等待2秒执行并发1000线程");
    Thread.sleep(2000);
    BARRIER.await();
    pool.shutdown();
  }

  /** 模拟获取数据或者业务处理 */
  public static void getData() {
    while (!store.tryAcquire()) {
      int a = 5000 + new Random().nextInt(1000);
      System.out.println("没有可用资源，等待一小会儿: " + a + "，目前：" + F.incrementAndGet());
      try {
        Thread.sleep(a);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    System.out.println("成功拿到资源");
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    store.release();
    System.out.println("释放资源，现在：" + S.incrementAndGet());
  }
}
