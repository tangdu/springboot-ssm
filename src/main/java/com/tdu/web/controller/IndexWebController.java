package com.tdu.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.tdu.dao.pojo.User;
import com.tdu.service.UserService;

@Controller
public class IndexWebController {

	public final String _PATH="com/tdu/";
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="login.html",method=RequestMethod.GET)
	public String loginPage(){
		return _PATH+"login";
	}
	
	@RequestMapping(value={"index.html","/"},method=RequestMethod.GET)
	public String indexPage(Model model){
		List<User> users=userService.findAll();
		model.addAttribute("users",users);
		return _PATH+"index";
	}
}
