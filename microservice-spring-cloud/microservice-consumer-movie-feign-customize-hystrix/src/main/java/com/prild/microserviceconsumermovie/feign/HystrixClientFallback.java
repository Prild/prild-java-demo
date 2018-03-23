package com.prild.microserviceconsumermovie.feign;

import com.prild.microserviceconsumermovie.feign.Entity.User;
import org.springframework.stereotype.Component;



@Component
public class HystrixClientFallback implements UserFeignClient {

  @Override
  public User findById(Long id) {
    User user = new User();
    user.setId(0L);
    return user;
  }
}