package com.prild.queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

//两个线程放数据，一个线程取数据
public class BlockingQueueTest {
	public static void main(String[] args) {
		final BlockingQueue queue = new ArrayBlockingQueue(3);		//长读为3的队列
		for(int i=0;i<2;i++){
			new Thread(){
				public void run(){
					while(true){
						try {
							Thread.sleep((long)(Math.random()*1000));
							System.out.println(Thread.currentThread().getName() + "准备放数据");
							queue.put(1);
							System.out.println(Thread.currentThread().getName() + "已经放好了，" +
										"队列目前有" + queue.size() + "个数据");
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
				}
				
			}.start();
		}
		
		new Thread(){
			public void run(){
				while(true){
					try {
						//将此处的睡眠数据分别改为100和1000，观察运行结果
						Thread.sleep(1000);
						System.out.println(Thread.currentThread().getName() + "准备取数据");
						queue.take();
						System.out.println(Thread.currentThread().getName() + "已取走数据，" +
								"队列目前有" + queue.size() + "个数据");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}.start();			
	}
}
