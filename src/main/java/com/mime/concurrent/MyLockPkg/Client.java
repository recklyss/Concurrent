package com.mime.concurrent.MyLockPkg;

public class Client {

    public static void main(String[] args){
        final Sequence s = new Sequence();
        for (int i = 0; i < 100; i++) {
            new Thread(s::getNext).start();
        }
    }
}
