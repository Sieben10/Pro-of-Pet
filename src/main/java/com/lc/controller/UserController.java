package com.lc.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.lc.entity.Trade;
import com.lc.service.MessageService;
import com.lc.utils.AlipayConfig1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lc.dao.UserDao;
import com.lc.entity.Order;
import com.lc.entity.Project;
import com.lc.entity.User;
import com.lc.service.OrderService;
import com.lc.service.ProjectService;
import com.lc.service.UserService;
import com.lc.utils.AuthorizationUtils;
import com.lc.utils.Page;
import com.lc.vo.UserAndSups;

@Controller
@RequestMapping("/user")
// 表示所有方法以 /user 为父路经
public class UserController {

	@Autowired
	UserService userService;
	@Autowired
	ProjectService projectService;
	@Autowired
	OrderService orderService;
	@Autowired
	AuthorizationUtils auth;


	//列出所有的用户
	@RequestMapping("/back/listUsers")
	public String listUser(Model model, Page page) {
		// List<User> users = userService.listUsers(page);
		List<User> users = userService.listUsers(page);
		page.setTotal(userService.total());
		model.addAttribute("users", users);
		model.addAttribute("page", page);
		return "/back/user/listUsers";
	}

	//删除用户
	@RequestMapping("/back/deleteUser")
	public String deleteUser(Integer id) {
		userService.deleteById(id);
		return "redirect:/user/back/listUsers";
	}
	//新增用户
	@RequestMapping("/back/editUser")
	public String editUser(Model model, Integer id) {
		model.addAttribute("fuckuser", userService.getById(id));
		return "/back/user/editUser";
	}
	//修改用户
	@RequestMapping("/back/updateUser")
	public String updateUser(User user) {
		userService.updateUser(user);
		return "redirect:/user/back/listUsers";
	}
	//查看用户详情
	@RequestMapping("/back/userDetails")
	public String userDetails(Model model, Integer id) {
		User user = userService.getById(id);
		model.addAttribute("user", user);
		List<Project> projects = projectService.getByUserId(id);
		model.addAttribute("projects", projects);
		List<UserAndSups> orders = orderService.getOrdersByUserId(id);
		model.addAttribute("orders", orders);
		return "/back/user/userDetails";
	}
	@Autowired
	MessageService messageService;


	@RequestMapping("tixianandchongzhi")
	public String tixianandchongzhi(Integer money,Integer type,String info,HttpSession session){

		int userId =(int) session.getAttribute("myId");
		User user = userService.getById(userId);
		Trade trade = new Trade();
		if(type==1){
			user.setMoney(user.getMoney()+money);
			trade.setInfo(info);
			info="【充值】 充值"+money+"元";
		}
		if(type==2){
			user.setMoney(user.getMoney()-money);
			trade.setInfo("银行卡提现"+money);
			info="【提现】提现"+money+"元";
		}
		userService.updateUser(user);
		trade.setMoney(money);
		trade.setUserId(userId);
		messageService.insertTrade(trade);

		messageService.sendMessageToSponstor(userId,info);
		return "redirect:/fore/home";
	}

	@RequestMapping("xiugaiyouxiang")
	public String xiugaiyouxiang(String newmail,HttpSession session){
		int userId =(int) session.getAttribute("myId");
		User user = userService.getById(userId);
		user.setMail(newmail);
		userService.updateUser(user);

		return "redirect:/fore/home";
	}


	@RequestMapping("zhifubaozhifu")
	public void zhifubaozhifu(Integer money, HttpServletResponse rep,HttpSession session) throws Exception{
//获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig1.gatewayUrl, AlipayConfig1.app_id, AlipayConfig1.merchant_private_key, "json", AlipayConfig1.charset, AlipayConfig1.alipay_public_key, AlipayConfig1.sign_type);

		//设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(AlipayConfig1.return_url);
		//alipayRequest.setNotifyUrl(AlipayConfig.notify_url);

		//商户订单号，商户网站订单系统中唯一订单号，必填
		String out_trade_no = UUID.randomUUID().toString();
		//付款金额，必填
		String total_amount = money.toString();
		//订单名称，必填
		String subject   ="用户充值";
		//商品描述，可空
		String body = "";

		alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
				+ "\"total_amount\":\""+ total_amount +"\","
				+ "\"subject\":\""+ subject +"\","
				+ "\"body\":\""+ body +"\","
				+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

		//请求
		String result = alipayClient.pageExecute(alipayRequest).getBody();
		session.setAttribute("zhifubaomoney",money);
		rep.setContentType("text/html;charset=" + AlipayConfig1.charset);
		rep.getWriter().write(result);//直接将完整的表单html输出到页面
		rep.getWriter().flush();
		rep.getWriter().close();

	}

	@RequestMapping("zhifubaoreturn")
	public String zhifubaoreturn(String newmail,HttpSession session){
		int userId =(int) session.getAttribute("myId");
		User user = userService.getById(userId);
		Integer money = (Integer) session.getAttribute("zhifubaomoney");
		user.setMoney(user.getMoney()+money);
		userService.updateUser(user);
		messageService.sendMessageToSponstor(userId,"【支付宝充值】");
		return "redirect:/fore/home";
	}

}
