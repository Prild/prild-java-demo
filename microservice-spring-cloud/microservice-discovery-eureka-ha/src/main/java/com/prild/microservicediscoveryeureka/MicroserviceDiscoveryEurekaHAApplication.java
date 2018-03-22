package com.prild.microservicediscoveryeureka;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer        //申明一个eureka server
public class MicroserviceDiscoveryEurekaHAApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceDiscoveryEurekaHAApplication.class, args);
	}
}
