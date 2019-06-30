package com.mime.concurrent.ExchangerStudy;

import cn.hutool.core.util.StrUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Exchanger;

/** 在对方线程调用exchange之前，另一个线程执行到exchange会阻塞 直到双方都调用exchange */
public class ExchangerStudy {

  private static ArrayBlockingQueue<String> initialFillQueue = new ArrayBlockingQueue<>(5);
  private static ArrayBlockingQueue<String> initialEmptyQueue = new ArrayBlockingQueue<>(5);
  private static Exchanger<ArrayBlockingQueue<String>> exchanger = new Exchanger<>();

  /** 填充缓存队列的线程 */
  static class FillingRunnable implements Runnable {
    @Override
    public void run() {
      ArrayBlockingQueue<String> current = initialEmptyQueue;
      try {
        while (current != null) {
          String str = StrUtil.uuid();
          System.out.println("生产了一个序列：" + str + ">>>>>加入到交换区");
          Thread.sleep(2000);
          try {
            current.add(str);
          } catch (IllegalStateException e) {
            System.out.println("队列已满，换一个空的");
            current = exchanger.exchange(current);
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {

      }
    }
  }
  /** 填充缓存队列的线程 */
  static class EmptyingRunnable implements Runnable {
    @Override
    public void run() {
      ArrayBlockingQueue<String> current = initialFillQueue;
      try {
        while (current != null) {
          if (!current.isEmpty()) {
            String str = current.poll();
            System.out.println("消耗一个数列：" + str);
          } else {
            System.out.println("队列空了，换个满的");
            current = exchanger.exchange(current);
            System.out.println("换满的成功~~~~~~~~~~~~~~~~~~~~~~");
          }
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {

      }
    }
  }

  public static void main(String[] args) {
    new Thread(new FillingRunnable()).start();
    new Thread(new EmptyingRunnable()).start();

  }
}
