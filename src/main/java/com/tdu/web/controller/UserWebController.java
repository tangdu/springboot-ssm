package com.tdu.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tdu.pojo.User;
import com.tdu.service.UserService;

@Controller
@RequestMapping("user")
public class UserWebController {

	@Autowired
	private UserService userService;

	@RequestMapping("add/{uname}")
	public @ResponseBody ResponseEntity<User> add(@PathVariable String uname) {
		User user = new User();
		user.setUname(uname);
		user.setUpwd("ad1");
		userService.save(user);
		ResponseEntity<User> users = new ResponseEntity<User>(user, HttpStatus.ACCEPTED);
		return users;
	}

	@RequestMapping("findAll")
	public @ResponseBody ResponseEntity<List<User>> findAll() {
		ResponseEntity<List<User>> users = new ResponseEntity<List<User>>(userService.findAll(), HttpStatus.ACCEPTED);
		return users;
	}
	
	@RequestMapping("delete/{id}")
	public @ResponseBody String  delete(@PathVariable String id) {
		userService.delete(id);
		return "ok";
	}
}
