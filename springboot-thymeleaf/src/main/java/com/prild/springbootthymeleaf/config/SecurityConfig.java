package com.prild.springbootthymeleaf.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 安全配置类
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/css/**","/js/**","/fonts/**","/index").permitAll()   //都可以访问
                .antMatchers("/users/**").hasRole("ADMIN")  //需要相应的角色才能访问
                .and()
                .formLogin()    //基于Form表单登录验证
                .loginPage("/login").failureUrl("/login-error");    //自定义登录界面
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()       //认证信息存储在内存中
                .withUser("aa")
                .password("123")
                .roles("ADMIN");
    }
}
