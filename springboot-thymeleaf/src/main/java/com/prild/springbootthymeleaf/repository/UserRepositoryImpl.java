package com.prild.springbootthymeleaf.repository;

import com.prild.springbootthymeleaf.pojo.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class UserRepositoryImpl implements UserRepository{
    private final ConcurrentMap<Integer,User> userMap =  new ConcurrentHashMap<>();
    private static AtomicInteger counter = new AtomicInteger();

    @Override
    public User saveOrupdateUser(User user) {
        Integer id = user.getId();
        if (id == null){//新建
            id = counter.incrementAndGet();
            user.setId(id);
        }
        this.userMap.put(id,user);
        return null;
    }

    @Override
    public void deleteUser(Integer id) {
        this.userMap.remove(id);
    }

    @Override
    public User getUserById(Integer id) {
        return this.userMap.get(id);
    }

    @Override
    public List<User> selectAllUsers() {
        return new ArrayList<User>(this.userMap.values());
    }
}
