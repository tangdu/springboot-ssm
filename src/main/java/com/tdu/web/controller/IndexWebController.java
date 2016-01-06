package com.tdu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexWebController {

	public final String _PATH="com/tdu/";
	
	@RequestMapping(value="login.html",method=RequestMethod.GET)
	public String loginPage(){
		return _PATH+"login";
	}
	
	@RequestMapping(value={"index.html","/"},method=RequestMethod.GET)
	public String indexPage(){
		return _PATH+"index";
	}
}
