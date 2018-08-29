package com.prild.thread.lock_condition;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockReadAndWriteTest {

    public static void main(String[] args) {
        final Queue3 q3 = new Queue3();
        for (int i = 0;i< 3;i++){
            new Thread(){
                public void run(){
                    while (true){
                        q3.get();
                    }
                }
            }.start();

            new Thread(){
                public void run(){
                    while (true){
                        int data = new Random().nextInt(1000);
                        q3.put(data);
                    }
                }
            }.start();
        }
    }
}

class Queue3{
    private Object data = null;//共享数据，只能有一个线程能写
    private ReadWriteLock rwl = new ReentrantReadWriteLock();

    public void get(){
        rwl.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "开始读数据");
            Thread.sleep((long)(Math.random()*1000));
            System.out.println(Thread.currentThread().getName() + "读完毕"+ data);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            rwl.readLock().unlock();
        }
    }

    public void put(Object data){
        rwl.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName()+ "开始写数据" + data);
            Thread.sleep((long)(Math.random()*1000));
            this.data = data;
            System.out.println(Thread.currentThread().getName()+ "写完毕"+ data);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            rwl.writeLock().unlock();
        }
    }
}
