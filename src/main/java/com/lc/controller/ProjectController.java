package com.lc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lc.entity.Project;
import com.lc.entity.Trade;
import com.lc.entity.User;
import com.lc.service.MessageService;
import com.lc.service.OrderService;
import com.lc.service.ProjectService;
import com.lc.service.UserService;
import com.lc.utils.AuthorizationUtils;
import com.lc.utils.Page;
import com.lc.vo.ProAndUsers;

@Controller
@RequestMapping("/pro")
public class ProjectController {

	@Autowired
	ProjectService projectService;

	@Autowired
	OrderService orderService;

	@Autowired
	UserService userService;
	@Autowired
	AuthorizationUtils auth;
	@Autowired
	MessageService messageService;

	// 前台管理操作开始
 
	@RequestMapping("/fore/updateState")
	public String updateForeState(Model model, Integer id, Integer state, HttpSession session,
			HttpServletRequest request) {
		if (!auth.check(session))
			return "redirect:/login/toLogin";
		projectService.updateState(id, state,null);
		messageService.sendMessage(id, state);

		Integer type = Integer.parseInt(request.getParameter("type"));
		int uid = (int) session.getAttribute("myId");
		List<Project> list = projectService.getByUserId(uid);
		model.addAttribute("projects", list);
		return "/fore/pro/pro_frg::frg" + type;

	}

 
	// 前台管理操作结束

	// 后台管理操作开始
	@RequestMapping("/listAllPros")
	public String listAllProjects(Model model, Page page) {
		List<Project> projects = projectService.listAllProjects(page);
		page.setTotal(projectService.total());
		model.addAttribute("projects", projects);
		model.addAttribute("page", page);
		return "/back/pro/allPros";
	}

	@RequestMapping("/fundingPros")
	public String fundingPros(Model model) {
		List<Project> list = projectService.getProsByState(21);
		model.addAttribute("projects", list);
		return "/back/pro/fundingPros";
	}

	@RequestMapping("/newApply")
	public String newApply(Model model) {
		List<Project> list = projectService.getProsByState(1);
		model.addAttribute("projects", list);
		return "/back/pro/newApply";
	}

	@RequestMapping("/drawApply")
	public String drawApply(Model model) {
		List<Project> list = projectService.getProsByState(41);
		model.addAttribute("projects", list);
		return "/back/pro/drawApply";
	}



	// 新增放款
	@RequestMapping("/back/updateState")
	public String updateState(Model model, Integer id, Integer state,String remark) {
		int userId = projectService.getById(id).getUserId();
		if (state == 5) {		

			int money = projectService.getById(id).getCurrentMoney();
			User user = userService.getById(userId);
			user.setMoney(user.getMoney() + money);
			userService.updateUser(user);
 			Trade trade = new Trade();
			trade.setMoney(money);
			trade.setInfo("提款到账");
			trade.setUserId(userId);
			messageService.insertTrade(trade);
		}

		   projectService.updateState(id, state,remark);
		if(!StringUtils.isEmpty(remark)){
			messageService.sendMessageToSponstor(userId, "【审核不通过】 "+remark);
		}
		return messageService.sendMessage(id, state);
	}



}
