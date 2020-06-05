package com.lc.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lc.entity.Message;
import com.lc.entity.Project;
import com.lc.entity.Trade;
import com.lc.entity.User;
import com.lc.service.MessageService;
import com.lc.service.OrderService;
import com.lc.service.ProjectService;
import com.lc.service.UserService;
import com.lc.utils.AuthorizationUtils;
import com.lc.vo.LoginVo;
import com.lc.vo.UserAndSups;

@Controller
@RequestMapping("/fore")
public class ForeHomeController {

	@Autowired
	AuthorizationUtils auth;
	@Autowired
	UserService userService;
	@Autowired
	ProjectService projectService;
	@Autowired
	OrderService orderService;
	@Autowired
	MessageService messageService;

	@RequestMapping("/home")
	public String home(Model model, HttpSession session) {

		if (!auth.check(session))
			return "redirect:/login/toLogin";

		int id = (int) session.getAttribute("myId");
		User user = userService.getById(id);
		model.addAttribute("user", user);
		List<UserAndSups> orders = orderService.getOrdersByUserId(id);
		model.addAttribute("orders", orders);

		List<Project> projects = projectService.getByUserId(id);
		model.addAttribute("projects", projects);

		List<Message> messages = messageService.getMessagesByUserId(id);
		model.addAttribute("messages", messages);

		List<Trade> trades = messageService.getTrade(id);
		model.addAttribute("trades", trades);

		return "/fore/usercenter";
	}

	// 映射到主页
	@RequestMapping("/index")
	public String toIndex(HttpSession session, Model model) {
		LoginVo loginVo = (LoginVo) session.getAttribute("loginVo"); // 验证是否登录
		if (loginVo != null) {
			model.addAttribute("flag", loginVo.getUsername());
		} else
			model.addAttribute("flag", "unlogin");


		Integer userID = (Integer) session.getAttribute("myId");//显示消息数
		if(userID==null || userID==0){
			model.addAttribute("messageNum",0);
		}else{
			model.addAttribute("messageNum", messageService.totalMessage(userID));
		}

		return "/index";
	}
	


}