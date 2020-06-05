package com.lc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/back")
public class BackHomeController {


	@RequestMapping("/home")
	public String home(String username,String password) {

		if("admin".equals(username) && "123456".equals(password)){
			return "/back/home";
		}

		return "/back/adminlogin";
	}
	@RequestMapping("/main")
	public String main() {	
		return "/back/main";
	}

	@RequestMapping("/adminlogin")
	public String adminlogin() {
		return "/back/adminlogin";
	}


}
