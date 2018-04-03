package com.prild.thread;

import javax.swing.tree.VariableHeightLayoutCache;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ThreadLocalTest {

    static ThreadLocal<Integer> x = new ThreadLocal<Integer>();

    public static void main(String[] args) {
        for (int i = 1;i <= 2; i++){
            new Thread(new Runnable() {
                public void run() {
                    int data = new Random().nextInt();
                    System.out.println( Thread.currentThread().getName() + "放置了数据"+data);
                    x.set(data);
                    new A().get();
                    new B().get();
                }
            }).start();
        }
    }

    static class A{
        public void get(){
            Integer data = x.get();
            System.out.println("A从" + Thread.currentThread().getName() + "获取到数据" + data );
        }
    }

    static class B{
        public void get(){
            Integer data = x.get();
            System.out.println("B从" + Thread.currentThread().getName() + "获取到数据" + data );
        }
    }
}

class User{
    private static ThreadLocal<User> map = new ThreadLocal<User>();
    private User(){
    }
    public static /*synchronized*/ User getThreadInstance(){
        User user = map.get();
        if (user == null){
            user = new User();
        }
        return user;
    }
}
