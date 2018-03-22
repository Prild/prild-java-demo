package com.prild.microserviceconsumermovie.feign;

import com.prild.microserviceconsumermovie.config.CustomizeConfiguration;
import com.prild.microserviceconsumermovie.feign.Entity.User;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "microservice-provider-user2",configuration = CustomizeConfiguration.class)
public interface UserFeignClient {
    //只能是用requestMapping注解
    //@RequestMapping(value = "/simple/{id}",method = RequestMethod.GET)
    @RequestLine("GET /simple/{id}")
    public User findById(@Param("id") Long id);

    //@RequestMapping(value = "/user",method = RequestMethod.POST)
    //public User postUser(@RequestBody User user);

    //不能执行
    //@RequestMapping(value = "/get-user",method = RequestMethod.GET)
    //public User getUser(User user);
}
