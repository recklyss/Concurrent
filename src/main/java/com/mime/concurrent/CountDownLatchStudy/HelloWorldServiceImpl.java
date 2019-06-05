package com.mime.concurrent.CountDownLatchStudy;

import cn.hutool.core.date.DateUtil;

import java.util.Date;
import java.util.Random;

/**
 * @Author zhangjiaheng
 * @Description
 **/
public class HelloWorldServiceImpl implements HelloworldService {
    @Override
    public void sayHello() {
        try {
            Thread.sleep(new Random().nextInt(2000));

            System.out.println(DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS") + "\t" + Thread.currentThread().getName() + " hello ... ...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
