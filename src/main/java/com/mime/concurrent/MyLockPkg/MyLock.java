package com.mime.concurrent.MyLockPkg;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 自定义锁 实现锁的获取与释放 以及可重入
 */
public class MyLock implements Lock {

    private boolean locked = false;

    private int lockCount;

    private Thread thread;

    public synchronized void lock() {
        try {
            // 自旋等待
            // 可重入：如果获取锁的不是当前线程并且当前已经有线程加锁，则等待
            while (locked && Thread.currentThread() != this.thread) {
                System.out.println("我等会儿。。。");
                wait();
            }
            this.thread = Thread.currentThread();
            lockCount++;
            locked = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    public synchronized void unlock() {
        try {
            if(this.thread == Thread.currentThread()){
                lockCount--;
                if(lockCount == 0 ){
                    notifyAll();
                    locked = false;
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
