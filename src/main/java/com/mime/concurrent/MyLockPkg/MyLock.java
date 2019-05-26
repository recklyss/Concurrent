package com.mime.concurrent.MyLockPkg;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 自定义锁 实现锁的获取与释放 以及可重入
 */
public class MyLock implements Lock {

    private boolean locked = false;// 当前锁是否已经被线程使用

    private int lockCount;// 保存当前线程加锁的次数

    private Thread thread;// 保存当前线程是哪一个

    public synchronized void lock() {
        try {
            // 自旋等待
            // 可重入：如果获取锁的不是当前线程并且当前已经有线程加锁，则等待
            while (locked && Thread.currentThread() != this.thread) {
                System.out.println("我等会儿。。。");
                wait();
            }
            // 如果没有线程使用锁或者获取锁的是当前线程 加锁计数器+1 然后thread指向获取锁的线程
            this.thread = Thread.currentThread();
            lockCount++;
            locked = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 释放锁的操作
     */
    public synchronized void unlock() {
        try {
            // 如果不是当前线程 不需要做任何操作
            if(this.thread == Thread.currentThread()){
                // 锁计数器减1 如果当前线程获取锁个数释放完成
                lockCount--;
                if(lockCount == 0 ){
                    // 释放完成 加锁标志置为false 再唤醒等待锁的线程
                    locked = false;
                    notifyAll();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public void lockInterruptibly() throws InterruptedException {

    }

    public boolean tryLock() {
        return false;
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    public Condition newCondition() {
        return null;
    }
}
