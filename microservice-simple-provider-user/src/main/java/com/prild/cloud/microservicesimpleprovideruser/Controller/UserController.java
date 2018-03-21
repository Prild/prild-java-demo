package com.prild.cloud.microservicesimpleprovideruser.Controller;

import com.prild.cloud.microservicesimpleprovideruser.Entity.User;
import com.prild.cloud.microservicesimpleprovideruser.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/simple/{id}")
    public Optional<User> findById(@PathVariable Long id){
        return userRepository.findById(id);
    }
}
