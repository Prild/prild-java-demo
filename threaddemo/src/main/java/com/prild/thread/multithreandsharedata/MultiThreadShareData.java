package com.prild.thread.multithreandsharedata;

public class MultiThreadShareData {

    private static ShareData1 data1 = new ShareData1();

    public static void main(String[] args) {
        /*买票逻辑
        ShareData1 shareData1 = new ShareData1();
        new Thread(shareData1).start();
        new Thread(shareData1).start();
        new Thread(shareData1).start();*/
        ShareData1 data2 = new ShareData1();
        new Thread(new MyRunnable1(data2)).start();
        new Thread(new MyRunnable2(data2)).start();

        //final ShareData1 data1 = new ShareData1();
        new Thread(new Runnable() {
            public void run() {
                data1.increment();
            }
        }).start();
        new Thread(new Runnable() {
            public void run() {
                data1.decrement();
            }
        }).start();
    }
}

class ShareData1 /*implements Runnable*/ {
    /*买票逻辑
    private int count = 100;

    public void run() {
        while (count > 0){
            count --;
            System.out.println(Thread.currentThread().getName() + "剩余" + count);
        }
    }*/

    private int j = 0;

    public synchronized void increment(){
        j ++;
    }

    public synchronized void decrement(){
        j --;
    }
}

class MyRunnable1 implements Runnable{

    private ShareData1 data1;

    public MyRunnable1(ShareData1 data1){
        this.data1 = data1;
    }

    public void run() {
        data1.decrement();
    }
}

class MyRunnable2 implements Runnable{

    private ShareData1 data1;

    public MyRunnable2(ShareData1 data1){
        this.data1 = data1;
    }

    public void run() {
        data1.increment();
    }
}


