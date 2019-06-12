package com.mime.concurrent.MyReentrantLockPkg;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Author zhangjiaheng
 * @Description 使用AQS重写一个可重入锁
 **/
public class MyReentrantLockByAQS implements Lock {

    private Sync sync = new Sync();

    private class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            Thread t = Thread.currentThread();
            // 如果第一个线程进来 可以拿到锁 则返回true

            // 如果第二个线程进来 如果不等于当前线程 返回false 否则更新当前线程值

            int state = getState();
            if (state == 0) {
                while (compareAndSetState(0, 1)) {
                    setExclusiveOwnerThread(t);
                    return true;
                }
            } else if (t == getExclusiveOwnerThread()) {
                // 当前线程再进来
                setState(getState() + 1);
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            // 锁的获取和释放时一一对应的
            // 调用此方法的线程肯定是当前线程
            if (Thread.currentThread() != getExclusiveOwnerThread()) {
                throw new RuntimeException();
            }
            int c = getState() - arg;
            boolean flag = false;
            if (c == 0) {
                setExclusiveOwnerThread(null);
                flag = true;
            }
            setState(c);
            return flag;
        }

        public ConditionObject newCondition() {
            return new ConditionObject();
        }
    }

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}
