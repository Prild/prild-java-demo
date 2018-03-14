package com.prild.springboot.springbooteasydemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {
    @Value("${age}")//配置注入
    private Integer age;

    @Value("${name}")
    private String name;

    @Autowired
    private User user;

    //@RequestMapping(value = {"/hello","/hi"},method = RequestMethod.GET)
    //@RequestMapping(value = "/hello/{id}",method = RequestMethod.GET)
    @RequestMapping(value = "/{id}/hello",method = RequestMethod.GET)
    //@GetMapping(value = "/{id}/hello")
    public String say(
            @PathVariable("id") Integer id,//@PathVariable 获取url中的数据
            @RequestParam(value = "name",required = false,defaultValue = "prild") String name//@RequestParam 获取请求参数的值
            ){
        //return "hello world";
        //return "age:" + age + ",name:" + name;
        //return user.toString();
        return id.toString() + "," + name;
    }
}
