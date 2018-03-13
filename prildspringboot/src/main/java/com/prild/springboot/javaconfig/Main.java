package com.prild.springboot.javaconfig;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        //通过JAVA配置来实例化Spring容器
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        //在Spring容器中获取Bean对象
        UserService userService = context.getBean(UserService.class);

        List<User> list = userService.queryUserList();
        for (User user:list) {
            System.out.println(user.toString());
        }
        //消费容器
        context.destroy();
    }
}
