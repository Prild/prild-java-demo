package com.prild.microserviceconsumermovie.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;

@Configuration
public class Configuration2 {
  @Bean
  public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
    return new BasicAuthRequestInterceptor("user", "password");
  }

  /*
  * 将默认的Hystrix.Builder修改为Feign.Builder,实现单个FeignClient的Hystrix禁用
   */
  @Bean
  @Scope("prototype")
  public Feign.Builder feignBuilder() {
    return Feign.builder();
  }
}
