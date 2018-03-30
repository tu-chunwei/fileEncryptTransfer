package com.tu.user.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;


@Service
public interface UserService {

	Map<String,Object> selectByPrimaryKey(Integer id);
	
	List<Map<String,Object>> getAll();
	
	Map<String,Object> addUser(Map<String,Object> map);
	
	int delete(Map<String,Object> map);
	
	int update(Map<String,Object> map);
}
