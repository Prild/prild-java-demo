package com.prild.thread;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

//时间调度
public class TraditionTimerTest {

    public static void main(String[] args) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("执行任务");
            }
        },10000,3000);//10秒后执行，每3秒执行一次
        while(true){
            System.out.println(new Date().getSeconds());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
