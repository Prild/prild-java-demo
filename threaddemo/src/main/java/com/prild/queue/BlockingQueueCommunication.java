package com.prild.queue;

import javax.swing.tree.VariableHeightLayoutCache;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

//用两个具有1个空间的队列来实现同步通知的功能。
public class BlockingQueueCommunication {
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
        //private boolean b = true;
        BlockingQueue<Integer> q1 = new ArrayBlockingQueue<Integer>(1);
        BlockingQueue<Integer> q2 = new ArrayBlockingQueue<Integer>(1);

        {
            try {
                q2.put(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void sub(int i){

            try {
                q1.put(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int j = 1; j<=9;j++){
                System.out.println("子线程执行了第" + j +"次，当前是第"+i+ "次循环，");
            }
            try {
                q2.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void main(int i){
            try {
                q2.put(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (int j = 1; j<=15;j++){
                System.out.println("主线程执行了第" + j +"次，当前是第"+i+ "次循环，");
            }
            try {
                q1.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

