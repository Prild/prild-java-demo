package com.thinkgem.jeesite.modules.inxedu.dao;


import java.util.Date;
import java.util.List;

import com.thinkgem.jeesite.common.persistence.annotation.MyBatisDao;
import com.thinkgem.jeesite.modules.inxedu.entity.letter.MsgReceive;
import com.thinkgem.jeesite.modules.inxedu.entity.letter.QueryMsgReceive;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

/**
 * @description 站内信Dao的接口
 * @author www.inxedu.com
 */
@MyBatisDao
public interface MsgReceiveDao {
    /**
     * 添加站内信
     */
    public int addMsgReceive(MsgReceive msgReceive);

    /**
     * 查询站内信收件箱
     *
     * @param msgReceive 站内信实体
     * @param page       分页参数
     * @return List<QueryMsgReceive> 站内信收件箱List
     * @throws Exception
     */
    public List<QueryMsgReceive> queryMsgReceiveByInbox(MsgReceive msgReceive, PageEntity page) throws Exception;


    /**
     * 删除站内信过期消息
     */
    public int delMsgReceivePast(Date time) throws Exception;

    /**
     * 删除收件箱
     *
     * @param msgReceive 站内信实体 通过站内信的id
     * @throws Exception
     */
    public int delMsgReceiveInbox(MsgReceive msgReceive) throws Exception;

    /**
     * 更新收件箱所有信为已读
     *
     * @param msgReceive 站内信实体
     * @throws Exception
     */
    public boolean updateAllReadMsgReceiveInbox(MsgReceive msgReceive) throws Exception;

    /**
     * 更新某种类型的站内信状态为已读
     *
     * @param msgReceive 传入type 传入type和站内信收信人id
     * @throws Exception
     */
    public boolean updateAllMsgReceiveReadByType(MsgReceive msgReceive) throws Exception;

    /**
     * 批量添加消息
     *
     * @param msgReceiveList 消息的list
     */
    public int addMsgReceiveBatch(List<MsgReceive> msgReceiveList);
    
    /**
     * 发送系统消息
     *
     * @param content 要发送的内容
     * @param cusId   用户id
     * @throws Exception
     */
    public String addSystemMessageByCusId(String content, int cusId) throws Exception;
}
