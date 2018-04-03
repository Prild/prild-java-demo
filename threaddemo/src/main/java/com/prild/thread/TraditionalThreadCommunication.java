package com.prild.thread;

public class TraditionalThreadCommunication {

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
}

class Business{
    private boolean b = true;
    public synchronized void sub(int i){
        while (!b){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int j = 1; j<=9;j++){
            System.out.println("子线程执行了第" + j +"次，当前是第"+i+ "次循环，");
        }
        b =false;
        this.notify();
    }

    public synchronized void main(int i){
        while (b){
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int j = 1; j<=15;j++){
            System.out.println("主线程执行了第" + j +"次，当前是第"+i+ "次循环，");
        }
        b = true;
        this.notify();
    }

}
