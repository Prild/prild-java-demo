package com.prild.springboot.springbootmybatis.mapper;

import com.prild.springboot.springbootmybatis.pojo.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.ResultSetType;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select name from user where name=#{name}")
    //@Options(keyProperty = "name",keyColumn = "title")
    //@ResultType(User.class)       //执行返回值类型,如果返回的字段名称与下面接口方法的返回值类型的字段名称一致，可忽略
    //@Results(value = @Result(property = "name",column = "title"))       //返回值的属性和字段名称不一样
    public List<User> likeName(String name);

    @Insert("insert into user(title) values(#{name})")
    @Options(useGeneratedKeys=true,keyProperty = "id",keyColumn = "id")
    public void save(User user);

    @Update("update user set name=#{name} where id=#{id}")
    public int updateNameById(User user);

}
