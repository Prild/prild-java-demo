package com.prild.springboot.demo;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.nio.charset.Charset;

@Controller
@SpringBootApplication
@Configuration
public class HelloApplication {
    @RequestMapping("hello")
    @ResponseBody
    public String hello(){
        return "hello world!你好 世界！";
    }

    //自定义消息转化器
    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter(){
        StringHttpMessageConverter converter  = new StringHttpMessageConverter(Charset.forName("ISO-8859-1"));
        return converter;
    }


    public static void main(String[] args){
        //SpringApplication.run(HelloApplication.class,args);
        SpringApplication app = new SpringApplication(HelloApplication.class);
        app.setBannerMode(Banner.Mode.OFF);//关闭Banner
        app.run(args);
    }
}
