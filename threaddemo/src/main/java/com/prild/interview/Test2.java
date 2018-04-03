package com.prild.interview;

import java.util.concurrent.Semaphore;
import java.util.concurrent.SynchronousQueue;

/*
现成程序中的Test类中的代码在不断地产生数据，然后交给TestDo.doSome()方法去处理，就好像
生产者在不断地产生数据，消费者在不断消费数据。请将程序改造成有10个线程来消费生产者产生的
数据，这些消费者都调用TestDo.doSome()方法去进行处理，故每个消费者都需要一秒才能处理完，
程序应保证这些消费者线程依次有序地消费数据，只有上一个消费者消费完后，下一个消费者才能消费
数据，下一个消费者是谁都可以，但要保证这些消费者线程拿到的数据是有顺序的。
 */
public class Test2 {
    public static void main(String[] args) {
        final Semaphore semaphore = new Semaphore(1);   //信号灯，换做锁也可以
        //同步阻塞队列，只有在数据被读取后，才能向队列中插入数据，否则被阻塞
        final SynchronousQueue<String> queue = new SynchronousQueue<String>();
        for (int i = 0;i < 10 ;i ++){
            new Thread(new Runnable() {
                public void run() {
                    try {
                        semaphore.acquire();
                        String input = queue.take();
                        String output = TestDo.doSome(input);
                        System.out.println(Thread.currentThread().getName()+ ":" + output);
                        semaphore.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        System.out.println("begin:" + (System.currentTimeMillis()/1000));
        for (int i = 0;i<10;i++){   //不能改动
            String input = i + "";//不能改动
            try {
                queue.put(input);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //String output = TestDo.doSome(input);
            //System.out.println(Thread.currentThread().getName()+ ":" + output);
        }
    }
}

/*class Test{
    public static void main(String[] args) {
        System.out.println("begin:" + (System.currentTimeMillis()/1000));
        for (int i = 0;i<10;i++){   //不能改动
            String input = i + "";//不能改动
            String output = TestDo.doSome(input);
            System.out.println(Thread.currentThread().getName()+ ":" + output);

        }
    }
}*/

class TestDo{
    public static String doSome(String input){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String output = input + ":" +(System.currentTimeMillis()/1000);
        return output;
    }
}
