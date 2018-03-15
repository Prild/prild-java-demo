package com.prild.springboot.springbootmybatis;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.prild.springboot.springbootmybatis.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/likeName")
    public List<User> likeName(String name){
        //PageHelper.startPage(1,2);
        List<User> users = userService.likeName(name);
        /*System.out.println("total:" + ((Page<User>)users).getTotal());
        for (User user : users) {
            System.out.println(user.getId());
            
        }*/
        return users;
    }

    @RequestMapping("/save")
    public User save(){
        User user = new User();
        user.setName("aa");
        //user.setId(1);
        userService.save(user);
        return user;
    }

    @RequestMapping("/update")
    public User update(){
        User user = new User();
        user.setId(4);
        user.setName("kk");
        userService.updateNameById(user);
        return user;
    }
}
