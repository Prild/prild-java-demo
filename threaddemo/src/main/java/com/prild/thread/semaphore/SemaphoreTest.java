package com.prild.thread.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


//信号灯，实现并发
public class SemaphoreTest {
    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        final Semaphore sp = new Semaphore(3);
        for (int i=0;i<10;i++){
            Runnable runnable = new Runnable() {
                public void run() {
                    try {
                        sp.acquire();   //获得
                    }catch (Exception e){
                        e.printStackTrace();
                    }                                                                                   //可获得的许可，即剩余可用信号灯
                    System.out.println("线程" + Thread.currentThread().getName() + "进入，当前已有" + (3-sp.availablePermits()));

                    try {
                        Thread.sleep((long)(Math.random()*10000));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    System.out.println("线程" + Thread.currentThread().getName() + "即将离开");
                    sp.release();   //发布，释放灯

                    //下面代码有时候执行不准确，因为其没有和上面的代码合成原子
                    System.out.println("线程" + Thread.currentThread().getName() + "已离开，当前已有" + (3-sp.availablePermits()));
                }
            };
            service.execute(runnable);
        }
    }
}
