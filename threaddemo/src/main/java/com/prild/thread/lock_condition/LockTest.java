package com.prild.thread.lock_condition;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockTest {

    public static void main(String[] args) {
        final NameOutputer nameOutputer = new NameOutputer();
        new Thread(new Runnable() {
            public void run() {
                while (true){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    nameOutputer.outPut("赵伊荔");
                }
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                while (true){
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    nameOutputer.outPut("张三三");
                }
            }
        }).start();
    }
}

class NameOutputer{
    Lock lock = new ReentrantLock();
    public void outPut(String name){
        int length = name.length();
        lock.lock();
        try{//  这里这么操作是为了防止执行下面任务过程中出现异常直接跳出而未释放锁
            for (int i = 0;i<length;i++){
                System.out.print(name.charAt(i));
            }
            System.out.println();
        }finally {
            lock.unlock();
        }
    }

    public synchronized void outPut2(String name){
        int length = name.length();

        for (int i = 0;i<length;i++){
            System.out.print(name.charAt(i));
        }
        System.out.println();

    }
}
