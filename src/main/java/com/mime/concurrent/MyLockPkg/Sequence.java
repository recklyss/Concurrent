package com.mime.concurrent.MyLockPkg;

/**
 * 一个计数器
 */
public class Sequence {

    private int value;

    private MyLock lock = new MyLock();

    Sequence(){
        this.value = 0;
    }

    /**
     * 获取下一个累加器的值
     */
    public int getNext(){
        lock.lock();
        this.value ++;
        this.sout();
        lock.unlock();
        return this.value;
    }

    /**
     * 检测是否可重入
     */
    public void sout(){
        lock.lock();
        System.out.println(this.value);
        lock.unlock();
    }


}
