package com.prild.interview;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/*
现有程序同时启动了4个线程去调用TestDo.doSome(key,value)方法，由于TestDo.doSome(key,value)方法
内的代码是先暂停1秒，然后再输出以秒为单位的当前时间值，所以，会打印出4个相同的时间值，如：
    4：4：1258199615
    1：1：1258199615
    3：3：1258199615
    1：2：1258199615
请修改代码，如果有几个线程调用TestDo.doSome(key,value)方法时，传递进去的key相等(equals比较为true),
则这几个线程应互斥排队输出结果，即当有两个线程的key都是"1"时，它们中的一个要比另外其他线程晚1秒输出结果。
    4：4：1258199615
    1：1：1258199615
    3：3：1258199615
    1：2：1258199616
 */
public class Test3 extends Thread{
    private TestDo1 testDo;
    private String key;
    private String value;

    public Test3(String key,String key2,String value){
        this.testDo = TestDo1.getInstance();
        /*
        常量"1"和"1"是同一个对象，下面这行代码就是要用"1"+""的方式产生新的对象，以实现
        值没有改变，但对象却不再是同一个的效果。
         */
        this.key = key + key2;
        /*
        a = "1" + "";
        b = "1" + "";   编译器在编译的时候会默认把它们编译为"1"，故它们三者是同一个对象
         */
        this.value = value;
    }

    public static void main(String[] args) {
        Test3 a = new Test3("1", "", "1");
        Test3 b = new Test3("1", "", "2");
        Test3 c = new Test3("3", "", "3");
        Test3 d = new Test3("4", "", "4");
        System.out.println("begin:" + (System.currentTimeMillis()/1000));
        a.start();
        b.start();
        c.start();
        d.start();
    }

    public void run(){
        testDo.doSome(key,value);
    }
}

/*class Test extends Thread{
    private TestDo1 testDo;
    private String key;
    private String value;

    public Test(String key,String key2,String value){
        this.testDo = TestDo1.getInstance();
        this.key = key + key2;
        this.value = value;
    }

    public static void main(String[] args) {
        Test a = new Test("1", "", "");
        Test b = new Test("1", "", "");
        Test c = new Test("1", "", "");
        Test d = new Test("1", "", "");
        System.out.println("begin:" + (System.currentTimeMillis()/1000));
        a.start();
        b.start();
        c.start();
        d.start();
    }

    public void run(){
        testDo.doSome(key,value);
    }
}*/

class TestDo1{
    private TestDo1(){
    }

    private static TestDo1 _instance= new TestDo1();
    public static TestDo1 getInstance(){
        return _instance;
    }

    //private HashSet keys = new HashSet();     //线程不安全，遍历的同时有其他线程在写入
    private CopyOnWriteArraySet keys = new CopyOnWriteArraySet();

    public void doSome(Object key,String value){
        Object o = key;
        if (!keys.contains(o)){
            keys.add(o);
        }else{
            for (Object oo:keys) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (o.equals(oo)){
                    o = oo;
                    break;
                }
            }
        }

        synchronized (o)
        //以大括号内的是需要局部同步的代码，不能改动
        {
            try {
                Thread.sleep(1000);
                System.out.println(key + ":" + value + ":" + (System.currentTimeMillis() / 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

/*
class TestDo1{
    private TestDo1(){
    }

    private static TestDo1 _instance= new TestDo1();
    public static TestDo1 getInstance(){
        return _instance;
    }

    public void doSome(Object key,String value){
        //以大括号内的是需要局部同步的代码，不能改动
        {
            try {
                Thread.sleep(1000);
                System.out.println(key + ":" + value + ":" + (System.currentTimeMillis() / 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
*/

