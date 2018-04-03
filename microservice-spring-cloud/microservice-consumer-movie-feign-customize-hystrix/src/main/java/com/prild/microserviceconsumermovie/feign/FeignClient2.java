package com.prild.microserviceconsumermovie.feign;


import com.prild.microserviceconsumermovie.config.Configuration2;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "xxxx", url = "http://localhost:8761/", configuration = Configuration2.class,
        fallback = FeignClient2Fallback.class)
public interface FeignClient2 {
    @RequestMapping(value = "/eureka/apps/{serviceName}")
    public String findServiceInfoFromEurekaByServiceName(@PathVariable("serviceName") String serviceName);
}
