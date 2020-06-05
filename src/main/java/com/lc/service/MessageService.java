package com.lc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lc.dao.MessageDao;
import com.lc.entity.Message;
import com.lc.entity.Project;
import com.lc.entity.Trade;
import com.lc.vo.ProAndUsers;

@Service
public class MessageService {
	@Autowired
	MessageDao messageDao;
	@Autowired
	ProjectService projectService;
 
	
	public String sendMessage(Integer id,Integer state) {
		String infoToSpon = null;
		String infoToSup = null;
		String url = null;
		Project project = projectService.getById(id);
		int currentMoney = project.getCurrentMoney();
		int userId = project.getUserId();
		switch (state) {
		case 20: 
			infoToSpon = "【审核失败】您发起的项目 " + project.getName() + " 因违法、违规或其他原因未能通过审核，请修改后重新提交";
 			url =  "redirect:/pro/newApply";
			break;
		case 21:
			infoToSpon = "【审核通过】您发起的项目 " + project.getName() + " 已通过审核! ";
			url = "redirect:/pro/newApply";
			break;

 
		case 5:
			infoToSpon = "【申请通过】您的提款申请通过审核，金额为 " + currentMoney;
			infoToSup = "您支持的项目 " + project.getName() + " 已提款，感谢您的支持 ";
			url = "redirect:/pro/drawApply";
			break;
		case 40:
			infoToSpon = "【申请失败】您的提款申请未能通过审核，如有问题请联系15883670409 ";
			url = "redirect:/pro/drawApply";
			break;	

		case 6:
			infoToSup = "【项目放款】您支持的项目 " + project.getName() + " 已放款，如有问题请联系15883670409 ";
			url = "redirect:/pro/fore/listMyPros";
			break;
		case 1:
 			url = "redirect:/pro/fore/listMyPros";
			break;
		case 41:
 			url = "redirect:/pro/fore/listMyPros";
			break;
		default:
			break;
		}
		//不为空就发消息
		if(infoToSpon != null) {
			sendMessageToSponstor(userId,infoToSpon);
		}
		if(infoToSup != null) {
			sendMessageToSupporter(id,infoToSup);
		}
		return url;
	}
	/**
	 * 将消息发送给项目的支持者
	 **/
	public void sendMessageToSupporter(Integer projectId,String info) {
		List<ProAndUsers> lists =  projectService.getProAndUsersByProId(projectId);	 
		for (ProAndUsers tmp : lists) {
			messageDao.insertMessage(tmp.getUserId(), info);
		}
	}
	public void sendMessageToSponstor(Integer userId,String info) {
		messageDao.insertMessage(userId, info);
	}
	public List<Message> getMessagesByUserId(Integer userId) {
		return messageDao.getMessageByUserId(userId);
 	}
	public void setState(Integer id) {
		messageDao.setState(id);
	}
	public void deleteMsg(Integer id) {
		messageDao.deleteMsg(id);
	}
	
	public void insertTrade(Trade trade) {
		messageDao.insertTrade(trade.getUserId(),trade.getInfo(),trade.getMoney());
	}
	public List<Trade> getTrade(Integer id ) {
		return messageDao.getTrade(id);
	}
	
	public Integer totalMessage(int id){
		return messageDao.totalMessage(id);
	}

}
