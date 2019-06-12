package com.mime.concurrent.DistributionMySqlReentrantLock;

import cn.hutool.core.util.StrUtil;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Author zhangjiaheng
 * @Description 使用数据库实现分布式可重入锁
 **/
public class MySqlReentrantLock implements Lock {

    /**
     * 要加锁的方法全限定名
     */
    private final String methodWholeName;

    MySqlReentrantLock() {
        // 不传使用这个可以加全局锁
        this.methodWholeName = "DEFAULT_METHOD_NAME";
    }

    MySqlReentrantLock(String methodName) {
        this.methodWholeName = methodName;
    }

    private ThreadLocal<String> UUID = new ThreadLocal<>();

    private Sync sync = new Sync();

    private class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            Thread thread = Thread.currentThread();
            int state = getState();
            String uuid = getUuid();
            if (state == 0) {
                // 如果还没有被锁
                while (JdbcExecutor.insertLock(methodWholeName, uuid)) {
                    setState(1);
                    setExclusiveOwnerThread(thread);
                    return true;
                }
            } else if (thread == Thread.currentThread()) {
                // 如果是线程重入
                setState(state + 1);
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (Thread.currentThread() != getExclusiveOwnerThread()) {
                throw new RuntimeException("不是同一个线程来释放锁");
            }
            int c = getState() - arg;// 释放一次
            setState(c);
            if (c == 0) {
                // 释放完了
                setExclusiveOwnerThread(null);
                // 删除数据库的那个数据
                try {
                    // 根据方法全限定名和UUID解锁
                    JdbcExecutor.delete(methodWholeName, getUuid());
                } catch (SQLException e) {
                    return false;
                }
                return true;
            }
            return false;
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

    public String getUuid() {
        String uuid = UUID.get();
        if (StrUtil.isBlank(uuid)) {
            uuid = StrUtil.uuid();
            UUID.set(uuid);
        }
        return uuid;
    }
}
