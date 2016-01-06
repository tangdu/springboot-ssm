package com.tdu.mapper;

import java.util.List;

import com.tdu.pojo.User;

public interface UserMapper {

	public List<User> findAll();
	
	public void delete(String id);
	
	public void add(User user);
}
