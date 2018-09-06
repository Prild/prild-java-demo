package com.thank.activiti.mapper;

import org.activiti.engine.impl.persistence.entity.TaskEntityImpl;
import org.activiti.engine.task.Task;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * description: mybaits自定义查询mapper
 *
 * @author xiefayang
 * 2018/6/22 9:51
 */
public interface MyCustomMapper {

    @Select("select * from ACT_RU_TASK")
    List<Map<String, Object>> findAll();
}
