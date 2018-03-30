package com.tu.user.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tu.user.dao.UserDao;
import com.tu.user.service.UserService;


@Service
public class UserServiceImpl implements UserService {

	@Resource
	private UserDao userDao;
	@Override
	public Map<String,Object> selectByPrimaryKey(Integer id) {
		return userDao.selectByPrimaryKey(id);
	}
	@Override
	public List<Map<String,Object>> getAll() {
		return userDao.getAll();
	}
	@Override
	public Map<String,Object> addUser(Map<String, Object> map) {
		Map<String,Object> idMap = new HashMap<String, Object>();
		userDao.addUser(map);
		idMap.put("id", map.get("id"));
		System.out.println(map.get("id"));
		return idMap;
	}
	@Override
	public int delete(Map<String, Object> map) {
		return userDao.delete(map);
	}
	@Override
	public int update(Map<String, Object> map) {
		userDao.update(map);
		return 0;
	}
}
