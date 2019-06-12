package com.mime.concurrent.ConditionStudy;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author zhangjiaheng
 * @Description 商城 上下线产品
 **/
public class Tmall {

    private static final int MAX_STORE = 10;

    private int goodsNum = 0;

    private ReentrantLock lock = new ReentrantLock();

    private Condition producer = lock.newCondition();

    private Condition consumer = lock.newCondition();

    public void onlineGoods() {
        lock.lock();
        try {
            while (goodsNum >= MAX_STORE) {
                producer.await();
                System.out.println("商品数量达到最大库存");
            }
            Thread.sleep(500);
            System.out.println("开始上架商品，然后通知可以购买，当前数目："+(++goodsNum));
            consumer.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void buyGoods() {
        lock.lock();
        try {
            while (goodsNum <= 0) {
                System.out.println("商品没有库存了，通知上架");
                consumer.await();
            }
            Thread.sleep(500);
            System.out.println("开始购买商品，然后通知可以继续上架，当前数目："+(--goodsNum));
            producer.signal();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
