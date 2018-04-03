package com.prild.thread.lock_condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
三个线程循环执行，第一个线程循环执行15次后第二个线程循环执行9次，然后第三个线程执行5次
 */
public class ThreeConditionCommunication {

    public static void main(String[] args) {
        final Business business = new Business();
        new Thread(new Runnable() {
            public void run() {
                for (int i = 1; i<=50;i++){
                    business.second(i);
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                for (int i = 1; i<=50;i++){
                    business.three(i);
                }
            }
        }).start();

        for (int i = 1; i<=50;i++){
            business.first(i);
        }
    }


    static class Business{
        Lock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();
        Condition condition3 = lock.newCondition();

        private int b = 1;

        public void three(int i){
            lock.lock();
            try {
                while (b != 3){
                    try {
                        condition3.await();  //等待
                        //this.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for (int j = 1; j<=5;j++){
                    System.out.println("第三个线程执行了第" + j +"次，当前是第"+i+ "次循环，");
                }
                b =1;
                //this.notify();
                condition1.signal(); //通知

            }finally {
                lock.unlock();
            }
        }

        public void second(int i){
            lock.lock();
            try {
                while (b != 2){
                    try {
                        condition2.await();  //等待
                        //this.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for (int j = 1; j<=9;j++){
                    System.out.println("第二个线程执行了第" + j +"次，当前是第"+i+ "次循环，");
                }
                b =3;
                //this.notify();
                condition3.signal(); //通知

            }finally {
                lock.unlock();
            }
        }

        public void first(int i){
            lock.lock();
            try {
                while (b != 1){
                    try {
                        //this.wait();
                        condition1.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for (int j = 1; j<=15;j++){
                    System.out.println("第一个线程执行了第" + j +"次，当前是第"+i+ "次循环，");
                }
                b = 2;
                //this.notify();
                condition2.signal();
            }finally {
                lock.unlock();
            }
        }

    }
}

