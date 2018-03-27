package com.prild.microserviceconsumermovie.feign;

import com.prild.microserviceconsumermovie.UserFeignClient;
import com.prild.microserviceconsumermovie.feign.Entity.User;
import org.springframework.stereotype.Component;

@Component
public class HystrixClientFallBack implements UserFeignClient{
    @Override
    public User findById(Long id) {
        User user = new User();
        user.setId(0L);
        return user;
    }
}
