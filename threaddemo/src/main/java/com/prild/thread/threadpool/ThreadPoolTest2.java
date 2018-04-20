package com.prild.thread.threadpool;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolTest2 {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        Date date = new Date();
        long time0 = date.getTime();
        for (int i = 0;i < 100; i ++){
            //dosomething();
            threadPool.execute(new Runnable() {
                public void run() {
                    try {
                        dosomething();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        threadPool.shutdown();

        System.out.println(new Date().getTime() - time0);
    }

    private static void dosomething() throws InterruptedException {
        Thread.sleep(500);
        System.out.println(Thread.currentThread().getName() + "-----doSomething");
    }
}
