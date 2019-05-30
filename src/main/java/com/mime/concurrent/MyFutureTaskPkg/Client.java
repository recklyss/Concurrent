package com.mime.concurrent.MyFutureTaskPkg;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @Author zhangjiaheng
 * @Description 客户端
 **/
public class Client {

    public static void main(String[] args) {
        Callable<String> myCall = () -> {
            Thread.sleep(5000);
            return "睡了5秒之后返回";
        };
        MyFutureTask<String> future = new MyFutureTask<>(myCall);
        Thread thread = new Thread(future);
        thread.start();

        try {
            String result = future.get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
