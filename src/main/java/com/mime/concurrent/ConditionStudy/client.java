package com.mime.concurrent.ConditionStudy;

/**
 * @Author zhangjiaheng
 * @Description 客户端测试
 **/
public class client {

    private static Tmall mall = new Tmall();

    private static Runnable producer = () -> {
        while (true) {
            mall.onlineGoods();
        }
    };

    private static Runnable consumer = () -> {
        while (true){
            mall.buyGoods();
        }
    };

    public static void main(String[] args) {
        new Thread(producer).start();
        new Thread(producer).start();
        new Thread(producer).start();
        new Thread(consumer).start();
        new Thread(consumer).start();
        new Thread(consumer).start();
    }
}
