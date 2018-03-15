package com.prild.springboot.springbootmybatis;

import com.prild.springboot.springbootmybatis.mapper.UserMapper;
import com.prild.springboot.springbootmybatis.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;


    public List<User> likeName(String name){
        return userMapper.likeName(name);
    }

    @Transactional
    public void save(User user){
        userMapper.save(user);
    }

    @Transactional
    public int updateNameById(User user){
        int i = userMapper.updateNameById(user);
        System.out.println(i);
        return i;
    }


}
