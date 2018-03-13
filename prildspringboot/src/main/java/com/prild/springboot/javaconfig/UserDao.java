package com.prild.springboot.javaconfig;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    public List<User> queryUserList(){
        ArrayList<User> result = new ArrayList<User>();

        for(int i = 0;i <10;i++){
            User user = new User();
            user.setUsername("username" + i);
            user.setPassword("password" + i);
            user.setAge(i+1);
            result.add(user);
        }
        return result;
    }
}
