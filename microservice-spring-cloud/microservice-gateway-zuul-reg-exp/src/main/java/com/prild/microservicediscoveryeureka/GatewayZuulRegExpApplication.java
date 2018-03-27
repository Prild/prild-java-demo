package com.prild.microservicediscoveryeureka;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.discovery.PatternServiceRouteMapper;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableZuulProxy
public class GatewayZuulRegExpApplication {

	/*
	使用正则表达式指定路由规则  serviceId-version（microservice-provider-user2-v1）
	 */
	@Bean
	public PatternServiceRouteMapper serviceRouteMapper(){
		return new PatternServiceRouteMapper("(?<name>^.+)-(?<version>v.+$)", "${version}/${name}");
	}

	public static void main(String[] args) {
		SpringApplication.run(GatewayZuulRegExpApplication.class, args);
	}
}
