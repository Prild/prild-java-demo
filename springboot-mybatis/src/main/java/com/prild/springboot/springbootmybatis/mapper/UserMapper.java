package com.prild.springboot.springbootmybatis.mapper;

import com.prild.springboot.springbootmybatis.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user where name=#{name}")
    public List<User> likeName(String name);

    @Insert("insert into user(name) values(#{name})")
    @Options(useGeneratedKeys=true,keyProperty = "id",keyColumn = "id")
    public void save(User user);

    @Update("update user set name=#{name} where id=#{id}")
    public int updateNameById(User user);

}
