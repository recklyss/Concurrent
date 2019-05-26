package com.mime.concurrent.MyLockPkg;

public class Sequence {

    private int value;

    private MyLock lock = new MyLock();

    Sequence(){
        this.value = 0;
    }

    public int getNext(){
        lock.lock();
        this.value ++;
        this.sout();
        lock.unlock();
        return this.value;
    }

    public void sout(){
        lock.lock();
        System.out.println(this.value);
        lock.unlock();
    }


}
