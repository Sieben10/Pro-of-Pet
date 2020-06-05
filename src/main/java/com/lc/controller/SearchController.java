//用户端搜索界面所对应的控制器
package com.lc.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lc.service.MessageService;
import com.lc.service.ProjectService;
import com.lc.vo.LoginVo;
import com.lc.vo.ProjectVo;


@Controller
@RequestMapping("/search")
public class SearchController {
	private static Logger log = LoggerFactory.getLogger(SearchController.class);
	//自动装载的服务层对象
	@Autowired
	private ProjectService projectService;
	@Autowired
	private MessageService messageService;

	//用于处理根据关键词来检索的请求
	//参数1：HttpSession，用于获取服务器session对象
	//参数2：String keyword,用于进行关键词检索
	//参数3：Model model，用于存储页面的模型
	@RequestMapping("/searchKeyword")
	public String searchPage(HttpSession session,String keyword,Model model) {
		LoginVo loginVo = (LoginVo) session.getAttribute("loginVo"); // 验证是否登录
		if (loginVo != null) {
			model.addAttribute("flag", loginVo.getUsername());
		} else
			model.addAttribute("flag", "unlogin");
		Integer userID = (Integer) session.getAttribute("myId");
		if(userID==null){
			model.addAttribute("messageNum",0);
		}else{

			model.addAttribute("messageNum", messageService.totalMessage(userID));
		}


		List<ProjectVo> listCommonWeal = projectService.getProjectByType(1);

		if(!StringUtils.isEmpty(keyword)){
			List<ProjectVo> listCommonWeal1 = new ArrayList<>();
			for (ProjectVo projectVo:listCommonWeal) {
                if(projectVo.getName().contains(keyword)){
					listCommonWeal1.add(projectVo);
				}
			}
			model.addAttribute("commonWeal", listCommonWeal1);
		}else{
			model.addAttribute("commonWeal", listCommonWeal);
		}
		return "/serach";
	}

}
