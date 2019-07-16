package com.mime.concurrent.SynchronizedIntegerError;

import java.util.concurrent.CountDownLatch;

/** @Author zhangjiaheng @Description */
public class SynchronizedIntegerErrorDemo {

  private static Integer cn = 0;

  private static final int size = 20;

  private static CountDownLatch cd = new CountDownLatch(size);

  public static void main(String[] args) {
    for (int j = 0; j < size; j++) {
      int finalJ = j;
      new Thread(
              () -> {
                try {
                  cd.countDown();
                  cd.await();
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                for (int i = 0; i < 5; i++) {
                  synchronized (cn) {
                    cn++;
                    System.out.println(
                        "cn" + finalJ + " = " + cn + "\t\t\t" + System.identityHashCode(cn));
                  }
                }
              })
          .start();
    }


    while (Thread.activeCount() < 2) {
      System.out.println("cn = " + cn);
    }
  }
}
