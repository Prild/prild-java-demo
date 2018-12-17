package com.thinkgem.jeesite.modules.inxedu.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.modules.inxedu.dao.MsgSystemDao;
import com.thinkgem.jeesite.modules.inxedu.entity.letter.MsgSystem;
import com.thinkgem.jeesite.modules.sys.entity.PageEntity;

@Service
@Transactional
public class MsgSystemDaoService implements MsgSystemDao{
@Autowired
private MsgSystemDao msgSystemDao;
	@Override
	public int addMsgSystem(MsgSystem msgSystem) throws Exception {
		// TODO Auto-generated method stub
		return msgSystemDao.addMsgSystem(msgSystem);
	}

	@Override
	public List<MsgSystem> queryMsgSystemList(@Param("param1")MsgSystem msgSystem) throws Exception {
		// TODO Auto-generated method stub
		return msgSystemDao.queryMsgSystemList(msgSystem);
	}

	@Override
	public int delMsgSystemById(String[] ids) throws Exception {
		// TODO Auto-generated method stub
		return msgSystemDao.delMsgSystemById(ids);
	}

	@Override
	public List<MsgSystem> queryMSListByLT(Date lastTime) throws Exception {
		// TODO Auto-generated method stub
		return msgSystemDao.queryMSListByLT(lastTime);
	}

	@Override
	public boolean updateMsgSystemPastTime(Date lastTime) throws Exception {
		return msgSystemDao.updateMsgSystemPastTime(lastTime);
		// TODO Auto-generated method stub
		
	}

}
