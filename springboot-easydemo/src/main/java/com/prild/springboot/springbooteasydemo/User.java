package com.prild.springboot.springbooteasydemo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "user")//将全局配置文件中 前缀属性为user的后续属性 进行映射
@Component  //注入到bean容器
public class User {
    private Integer age;
    private String myname;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getMyname() {
        return myname;
    }

    public void setMyname(String myname) {
        this.myname = myname;
    }

    @Override
    public String toString() {
        return "User{" +
                "age=" + age +
                ", myname='" + myname + '\'' +
                '}';
    }
}
