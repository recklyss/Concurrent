package com.mime.concurrent.MyFutureTaskPkg;

import java.util.concurrent.*;

/**
 * @Author zhangjiaheng
 * @Description 自定义future类
 **/
public class MyFutureTask<V> implements Runnable, Future {

    private Callable<V> callable;

    public MyFutureTask(Callable<V> callable){
        this.callable = callable;
    }

    private V result;

    public void run() {
        synchronized (this){
            try {
                result = callable.call();
                this.notify();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    public boolean isCancelled() {
        return false;
    }

    public boolean isDone() {
        return false;
    }

    public synchronized V get() throws InterruptedException, ExecutionException {
        if(null != result){
            return result;
        }
        this.wait();
        return result;
    }

    public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return null;
    }
}
