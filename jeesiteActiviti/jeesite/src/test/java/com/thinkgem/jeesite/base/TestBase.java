package com.thinkgem.jeesite.base;

import org.junit.Test;
import org.junit.runner.RunWith; 
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:spring-context.xml" })
public class TestBase {
	ApplicationContext app = new ClassPathXmlApplicationContext(new String[]{"spring-context.xml","spring-context-shiro.xml"
			,"spring-context-jedis.xml","spring-context-activiti.xml","mybatis-config.xml"});

	   @Test
	    public void testList(){
		   
	   }
}
