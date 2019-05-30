package com.mime.concurrent.MyReentrantLockPkg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Author zhangjiaheng
 * @Description
 **/
public class Sequence {
    int value;

    public int getNext() {
        return value++;
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        Sequence sequence = new Sequence();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int k = sequence.getNext();
                System.out.println(Thread.currentThread().getName() + " --- " + Thread.currentThread().getId() + " --- " + k);
                list.add(k);
            }).start();
        }

        while (true){
            if(Thread.activeCount() < 2){
                Collections.sort(list);
                for (int i = 0; i < list.size(); i++) {
                    System.out.print(list.get(i) + " " );
                }
                return;
            }
        }
    }
}
