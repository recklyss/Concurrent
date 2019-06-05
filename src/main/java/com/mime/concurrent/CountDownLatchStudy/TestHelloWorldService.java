package com.mime.concurrent.CountDownLatchStudy;

/**
 * @Author zhangjiaheng
 * @Description
 **/
public class TestHelloWorldService {

    public static void main(String[] args) throws InterruptedException {
        HelloworldService hello = new HelloWorldServiceImpl();
        CountDownLatchUtil c = new CountDownLatchUtil(1000);
        c.latch(hello::sayHello);
    }
}
