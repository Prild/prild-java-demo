package com.prild.springbootthymeleaf;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping(value = "/hello")
    private String say(){
        return "say hello";
    }
}
