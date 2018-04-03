package com.prild.thread;

import java.util.concurrent.*;

public class CallableAndFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        Future<Integer> future = threadPool.submit(new Callable<Integer>() {
            public Integer call() throws Exception {
                System.out.println("执行任务");
                return 2;
            }
        });
        System.out.println("结果是" + future.get());
    }
}
