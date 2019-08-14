package com.mime.concurrent.ForkJoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/** @Author zhangjiaheng @Description fork/join学习 计算任务 */
public class MyCalculateTask extends RecursiveTask<Integer> {

  private static final int THREADSHOLD = 50;

  private int start;
  private int end;
  private List<String> list;

  public MyCalculateTask(int start, int end, List<String> list) {
    this.start = start;
    this.end = end;
    this.list = list;
  }

  @Override
  protected Integer compute() {
    int sum = 0;
    if (end - start < THREADSHOLD) {
      String so = "";
      for (int i = start; i < end; i++) {
        sum += i;
        so += list.get(i) + ",";
      }
      try {
        Thread.sleep(20);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println(Thread.currentThread().getName() + "处理 " + so + " 的数据");
    } else {
      int mid = (start + end) / 2;
      final MyCalculateTask left = new MyCalculateTask(start, mid, list);
      final MyCalculateTask right = new MyCalculateTask(mid, end, list);
      left.fork();
      right.fork();
      sum += left.join();
      sum += right.join();
    }

    return sum;
  }

  public static void main(String[] args) throws InterruptedException, ExecutionException {
    int sum = 0;
    ForkJoinPool pool = new ForkJoinPool();
    int count = 4000;
    List<String> list = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      list.add("i-" + i);
      sum += i;
    }
    MyCalculateTask task = new MyCalculateTask(0, count, list);
    final ForkJoinTask<Integer> submit = pool.submit(task);
    System.out.println("sum = " + sum + " --- submit.get() = " + submit.get());
    pool.shutdown();
  }
}
