package com.prild.thread.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {

    public static void main(String[] args) {
        //ExecutorService threadPool = Executors.newFixedThreadPool(3); //固定长度的线程池
        //ExecutorService threadPool = Executors.newCachedThreadPool();   //缓存的线程池
        ExecutorService threadPool = Executors.newSingleThreadExecutor();   //单一线程池
        for (int i = 1;i <= 12; i++){
            final int task = i;
            threadPool.execute(new Runnable() {
                public void run() {
                    for (int j = 0 ;j < 10 ;j ++){
                        System.out.println(Thread.currentThread().getName() + "......"+ j + ",在循环" + task);
                    }
                }
            });
        }
        threadPool.shutdown();

        /*Executors.newScheduledThreadPool(3).schedule(new Runnable() {
            public void run() {
                System.out.println("执行了");
            }
        },10, TimeUnit.SECONDS);*/

        /*Executors.newScheduledThreadPool(3).scheduleAtFixedRate(new Runnable() {
            public void run() {
                System.out.println("执行了");
            }
        },10, 2,TimeUnit.SECONDS);*/

        Executors.newScheduledThreadPool(3).scheduleWithFixedDelay(new Runnable() {
            public void run() {
                System.out.println("执行了");
            }
        },10,10, TimeUnit.SECONDS);

    }
}
