package com.thinkgem.jeesite.modules.inxedu.entity;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.format.annotation.DateTimeFormat;

import com.thinkgem.jeesite.common.persistence.DataEntity;


/**
 * 查询用户
 * @author www.inxedu.com
 *
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class QueryStudentUser extends DataEntity<QueryStudentUser>{
	private static final long serialVersionUID = 1L;
	private int isavalible;
	private String keyWord;
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date beginCreateTime;//查询 开始注册时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date endCreateTime;//查询 结束注册时间
}
