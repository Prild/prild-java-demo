package com.prild.microserviceconsumermovie.feign.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.prild.microserviceconsumermovie.feign.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MovieController {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/movie/{id}")
    @HystrixCommand(fallbackMethod = "findByIdFallback")   //指定该方法的回退方法
    public User findById(@PathVariable Long id){        //VIP   virtual IP  虚拟IP
        ServiceInstance instance = this.loadBalancerClient.choose("microservice-provider-user2");
        System.out.println("user:" + instance.getHost() + ":" + instance.getPort());

        return this.restTemplate.getForObject("http://microservice-provider-user2/simple/" + id,User.class);
    }

    public User findByIdFallback(Long id){
        User user = new User();
        user.setId(0L);
        return user;
    }


}
