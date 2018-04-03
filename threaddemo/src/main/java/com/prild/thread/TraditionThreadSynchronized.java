package com.prild.thread;

public class TraditionThreadSynchronized {

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
                    nameOutputer.outPut2("赵伊荔");
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
                    nameOutputer.outPut2("张三三");
                }
            }
        }).start();
    }
}

class NameOutputer{
    public void outPut(String name){
        int length = name.length();
        synchronized (this){
            for (int i = 0;i<length;i++){
                System.out.print(name.charAt(i));
            }
            System.out.println();
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
