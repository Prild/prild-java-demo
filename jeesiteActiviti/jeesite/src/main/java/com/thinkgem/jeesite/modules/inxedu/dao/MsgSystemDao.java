package com.thinkgem.jeesite.modules.inxedu.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inxedu.entity.letter.MsgSystem;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

/**
 * @description 站内信发件箱的Dao
 * @author www.inxedu.com
 */
@MyBatisDao
public interface MsgSystemDao {
    /**
     * 添加系统消息
     *
     * @param msgSender
     * @return
     * @throws Exception
     */
    public int addMsgSystem(MsgSystem msgSystem) throws Exception;

    /**
     * 查询系统消息
     *
     * @param msgSystem
     * @return
     * @throws Exception
     */
    public List<MsgSystem> queryMsgSystemList(@Param("param1")MsgSystem msgSystem) throws Exception;

    /**
     * 通过id删除系统消息
     */
    public int delMsgSystemById(String[] ids) throws Exception;

    /**
     * 查询大于传入的时间的系统系统消息
     */
    public List<MsgSystem> queryMSListByLT(Date lastTime) throws Exception;

    /**
     * 更新过期的系统消息的字段为过期
     */
    public boolean updateMsgSystemPastTime(Date lastTime) throws Exception;
}
