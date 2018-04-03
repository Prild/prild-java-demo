package com.prild.thread.lock_condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/*
主线程循环执行15次后子线程循环执行9次，如此循环交替
 */
public class ConditionCommunication {

    public static void main(String[] args) {
        final Business business = new Business();
        new Thread(new Runnable() {
            public void run() {
                for (int i = 1; i<=50;i++){
                    business.sub(i);
                }
            }
        }).start();

        for (int i = 1; i<=50;i++){
            business.main(i);
        }
    }


    static class Business{
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        private boolean b = true;
        public void sub(int i){
            lock.lock();
            try {
                while (!b){
                    try {
                        condition.await();  //等待
                        //this.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for (int j = 1; j<=9;j++){
                    System.out.println("子线程执行了第" + j +"次，当前是第"+i+ "次循环，");
                }
                b =false;
                //this.notify();
                condition.signal(); //通知

            }finally {
                lock.unlock();
            }
        }

        public void main(int i){
            lock.lock();
            try {
                while (b){
                    try {
                        //this.wait();
                        condition.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for (int j = 1; j<=15;j++){
                    System.out.println("主线程执行了第" + j +"次，当前是第"+i+ "次循环，");
                }
                b = true;
                //this.notify();
                condition.signal();
            }finally {
                lock.unlock();
            }
        }

    }
}

