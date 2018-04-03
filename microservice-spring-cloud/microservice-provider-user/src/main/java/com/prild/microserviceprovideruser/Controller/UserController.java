package com.prild.microserviceprovideruser.Controller;

import com.google.common.collect.Lists;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.prild.microserviceprovideruser.Entity.User;
import com.prild.microserviceprovideruser.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Qualifier("eurekaClient")
    @Autowired
    private EurekaClient eurekaClient;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/simple/{id}")
    public User findById(@PathVariable Long id){
        return userRepository.findOne(id);
    }

    @GetMapping("/eureka-instance")
    public String serviceUrl(){
        InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("MICROSERVICE-PROVIDER-USER2", false);
        return instanceInfo.getHomePageUrl();
    }

    @GetMapping("/instance-info")
    public ServiceInstance showInfo(){
        ServiceInstance instance = discoveryClient.getLocalServiceInstance();
        return instance;
    }

    @PostMapping("/user")
    public User postUser(@RequestBody User user) {
        return user;
    }


    @GetMapping("/get-user")
    public User getUser(User user) {
        return user;
    }

    @GetMapping("list-all")
    public List<User> listAll() {
        ArrayList<User> list = Lists.newArrayList();
        User user = new User(1L, "zhangsan");
        User user2 = new User(2L, "zhangsan");
        User user3 = new User(3L, "zhangsan");
        list.add(user);
        list.add(user2);
        list.add(user3);
        return list;

    }
}
