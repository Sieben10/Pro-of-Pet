package com.lc.utils;
/*
@Author  Dell
@Desc    用于对会话进行身份认证
@Date    2020年4月18日
*/
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lc.entity.User;
import com.lc.service.UserService;
import com.lc.vo.LoginVo;

@Service
public class AuthorizationUtils {

	@Autowired
	 UserService userService;

	public boolean check(HttpSession session) {
 
		if(session == null) {
			return false;
		}
		LoginVo loginVo = (LoginVo) session.getAttribute("loginVo");
		if (loginVo == null) {
			return false;
		}
		String username = loginVo.getUsername();
		User user = userService.getByName(username);
		if (user == null) {
			user = userService.getByPhone(username);
			if (user == null) {
				user = userService.getByMail(username);
				if (user == null) {
					return false;
				}
			}
		}
		String password = loginVo.getPassword();
		if (!password.equals(user.getPassword())) {
			return false;
		}
		return true;
	}
}
