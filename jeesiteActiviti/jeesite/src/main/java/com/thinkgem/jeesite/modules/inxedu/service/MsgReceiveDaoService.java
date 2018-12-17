package com.thinkgem.jeesite.modules.inxedu.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.inxedu.dao.MsgReceiveDao;
import com.thinkgem.jeesite.modules.inxedu.entity.StudentUser;
import com.thinkgem.jeesite.modules.inxedu.entity.letter.LetterConstans;
import com.thinkgem.jeesite.modules.inxedu.entity.letter.MsgReceive;
import com.thinkgem.jeesite.modules.inxedu.entity.letter.QueryMsgReceive;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

@Service
@Transactional
public class MsgReceiveDaoService implements MsgReceiveDao{
@Autowired
private MsgReceiveDao msgReceiveDao;
@Autowired
private StudentUserService studentUserService;
	@Override
	public int addMsgReceive(MsgReceive msgReceive) {
		// TODO Auto-generated method stub
		return msgReceiveDao.addMsgReceive(msgReceive);
	}

	@Override
	public List<QueryMsgReceive> queryMsgReceiveByInbox(MsgReceive msgReceive, PageEntity page) throws Exception {
		// TODO Auto-generated method stub
		return msgReceiveDao.queryMsgReceiveByInbox(msgReceive, page);
	}

	@Override
	public int delMsgReceivePast(Date time) throws Exception {
		// TODO Auto-generated method stub
		return msgReceiveDao.delMsgReceivePast(time);
	}

	@Override
	public int delMsgReceiveInbox(MsgReceive msgReceive) throws Exception {
		// TODO Auto-generated method stub
		return msgReceiveDao.delMsgReceiveInbox(msgReceive);
	}

	@Override
	public boolean updateAllReadMsgReceiveInbox(MsgReceive msgReceive) throws Exception {
		return msgReceiveDao.updateAllReadMsgReceiveInbox(msgReceive);
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean updateAllMsgReceiveReadByType(MsgReceive msgReceive) throws Exception {
		return msgReceiveDao.updateAllMsgReceiveReadByType(msgReceive);
		// TODO Auto-generated method stub
		
	}

	@Override
	public int addMsgReceiveBatch(List<MsgReceive> msgReceiveList) {
		// TODO Auto-generated method stub
		return msgReceiveDao.addMsgReceiveBatch(msgReceiveList);
	}
    /**
     * 发送系统消息
     *
     * @param content 要发送的内容
     * @param cusId   用户id
     * @throws Exception
     */

	@Override
	public String addSystemMessageByCusId(String content, int cusId) throws Exception {
		// TODO Auto-generated method stub
        StudentUser userExpandDto = studentUserService.queryUserById(cusId);
        
        MsgReceive msgReceive = new MsgReceive();
        msgReceive.setContent(content);// 添加站内信的内容
        msgReceive.setCusId( 0 );
        msgReceive.setReceivingCusId(cusId);// 要发送的用户id
        msgReceive.setStatus(LetterConstans.LETTER_STATUS_UNREAD);// 消息未读状态
        msgReceive.setType(LetterConstans.LETTER_TYPE_SYSTEMINFORM);// 系统消息
        msgReceive.setUpdateTime(new Date());// 更新时间s
        msgReceive.setAddTime(new Date());// 添加时间
        if (userExpandDto != null && userExpandDto.getShowName() != null) {// 如果不为空则set showname
            msgReceive.setShowname(userExpandDto.getShowName());// 会员名
        } else {// 如果为空则set 空字符串
            msgReceive.setShowname("");// 会员名
        }
        try{
        	msgReceiveDao.addMsgReceive(msgReceive);
        	studentUserService.updateUnReadMsgNumAddOne("sysMsgNum", cusId);
        }catch(Exception e){
        }
        
        return "success";
	}
	
}
