package com.prild.microserviceconsumermovie;

import com.prild.microserviceconsumermovie.Entity.User;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("microservice-provider-user2")
public interface UserFeignClient {
    @GetMapping("/simple/{id}")
    public User findById(@PathVariable Long id);
}
