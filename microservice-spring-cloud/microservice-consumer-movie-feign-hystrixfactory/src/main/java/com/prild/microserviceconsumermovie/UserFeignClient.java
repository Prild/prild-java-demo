package com.prild.microserviceconsumermovie;

import com.prild.microserviceconsumermovie.feign.Entity.User;
import com.prild.microserviceconsumermovie.feign.HystrixClientFallBack;
import com.prild.microserviceconsumermovie.feign.HystrixClientFallBackFactory;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;
                                                    //fallback与fallbackFactory有冲突
@FeignClient(name = "microservice-provider-user2",/*fallback = HystrixClientFallBack.class,*/fallbackFactory = HystrixClientFallBackFactory.class)
public interface UserFeignClient {
    //只能是用requestMapping注解
    @RequestMapping(value = "/simple/{id}",method = RequestMethod.GET)
    public User findById(@PathVariable("id") Long id);


}
