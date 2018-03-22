package com.prild.microserviceconsumermovie;

import com.prild.microserviceconsumermovie.feign.Entity.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient("microservice-provider-user2")
public interface UserFeignClient {
    //只能是用requestMapping注解
    @RequestMapping(value = "/simple/{id}",method = RequestMethod.GET)
    public User findById(@PathVariable("id") Long id);

    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public User postUser(@RequestBody User user);

    //不能执行
    @RequestMapping(value = "/get-user",method = RequestMethod.GET)
    public User getUser(User user);
}
