package com.prild.thread.lock_condition;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CacheDemo {

    private Map<String ,Object> cache = new HashMap();
    public static void main(String[] args) {


    }

    private ReadWriteLock readWriteLock =  new ReentrantReadWriteLock();
    public synchronized Object getData(String key){
        readWriteLock.readLock().lock();
        Object o = null;
        try {
            o = cache.get(key);
            if (o == null){
                readWriteLock.readLock().unlock();
                readWriteLock.writeLock().lock();
                try {
                    if (o == null)
                        o  = "aaa"; //查数据库
                }finally {
                    readWriteLock.writeLock().unlock();
                }
                readWriteLock.readLock().lock();
            }
        }finally {
            readWriteLock.readLock().unlock();
        }

        return o;
    }
}
