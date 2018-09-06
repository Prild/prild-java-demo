package com.thank.activiti.coreapi;

import com.google.common.collect.ImmutableMap;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.*;
import org.activiti.engine.test.ActivitiRule;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class IdentityServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(IdentityServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    @Test
    public void testIdentity(){
        IdentityService identityService = activitiRule.getIdentityService();
        User user1 = identityService.newUser("user01");
        User user2 = identityService.newUser("user02");
        user1.setEmail("user01@123.com");
        user2.setEmail("user02@123.com");
        identityService.saveUser(user1);
        identityService.saveUser(user2);

        Group group1 = identityService.newGroup("group01");
        Group group2 = identityService.newGroup("group02");
        identityService.saveGroup(group1);
        identityService.saveGroup(group2);

        identityService.createMembership("user01", "group01");
        identityService.createMembership("user01", "group02");
        identityService.createMembership("user02", "group01");

        User user01 = identityService.createUserQuery().userId("user01").singleResult();
        user01.setFirstName("userLast");
        identityService.saveUser(user01);

        List<User> userList = identityService.createUserQuery().memberOfGroup("group01").listPage(0, 100);
        userList.forEach(user -> logger.debug("user: {}", ToStringBuilder.reflectionToString(user,ToStringStyle.JSON_STYLE)));
        Assert.assertEquals(2, userList.size());

        List<Group> groupList = identityService.createGroupQuery().groupMember("user01").list();
        groupList.forEach(group -> logger.debug("group: {}", ToStringBuilder.reflectionToString(group, ToStringStyle.JSON_STYLE)));
        Assert.assertEquals(2, groupList.size());
    }

}



