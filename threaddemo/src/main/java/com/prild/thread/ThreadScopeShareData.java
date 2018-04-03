package com.prild.thread;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ThreadScopeShareData {

    //private static int data = 0;
    private static Map<Thread,Integer> map = new HashMap<Thread,Integer>();
    public static void main(String[] args) {
        for (int i = 1;i <= 2; i++){
            new Thread(new Runnable() {
                public void run() {
                    int data = new Random().nextInt();
                    System.out.println( Thread.currentThread().getName() + "放置了数据"+data);
                    map.put(Thread.currentThread(),data);
                    new A().get();
                    new B().get();
                }
            }).start();
        }
    }

    static class A{
        public void get(){
            Integer data = map.get(Thread.currentThread());
            System.out.println("A从" + Thread.currentThread().getName() + "获取到数据" + data );
        }
    }

    static class B{
        public void get(){
            Integer data = map.get(Thread.currentThread());
            System.out.println("B从" + Thread.currentThread().getName() + "获取到数据" + data );
        }
    }



}
