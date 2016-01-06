package com.tdu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tdu.mapper.UserMapper;
import com.tdu.pojo.User;

@Service
@Transactional
public class UserService {

	private static final String CACHE_NAME="user";
	@Autowired
	UserMapper userMapper;

	//@Cacheable(value=CACHE_NAME,key="\"user_all\"")
	public List<User> findAll() {
		return userMapper.findAll();
	}
	
	public void delete(String id) {
		userMapper.delete(id);
	}

	public void save(User user) {
		userMapper.add(user);
	}
}
