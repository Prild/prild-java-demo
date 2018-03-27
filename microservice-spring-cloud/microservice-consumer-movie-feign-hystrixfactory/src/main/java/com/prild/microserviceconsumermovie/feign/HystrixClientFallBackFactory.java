package com.prild.microserviceconsumermovie.feign;

import com.prild.microserviceconsumermovie.UserFeignClient;
import com.prild.microserviceconsumermovie.feign.Entity.User;
import feign.hystrix.FallbackFactory;

import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class HystrixClientFallBackFactory implements FallbackFactory<UserFeignClient>{
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(HystrixClientFallBackFactory.class);

    @Override
    public UserFeignClient create(Throwable throwable) {
        log.info("fallback; reason was: " + throwable.getMessage());
        return new UserFeignClientWithFactory() {
            @Override
            public User findById(Long id) {
                User user = new User();
                user.setId(-1L);
                return user;
            }
        };
    }
}
