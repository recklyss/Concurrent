package com.mime.concurrent.DistributionMySqlReentrantLock;

/**
 * @Author zhangjiaheng
 * @Description
 **/
public class client {

    private static int count = 0;

    private static void addCount() {
        MySqlReentrantLock lock = new MySqlReentrantLock("com.mime.concurrent.DistributionMySqlReentrantLock.client.addCount");
        lock.lock();
        try {
            for (int i = 0; i < 50; i++) {
                Thread.sleep(1000);
                count++;
                System.out.println("当前线程：" + Thread.currentThread().getName() + " --- count=" + count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    static Runnable f = client::addCount;

    public static void main(String[] args) {
        new Thread(f).start();
        new Thread(f).start();
        new Thread(f).start();
        new Thread(f).start();
    }
}
