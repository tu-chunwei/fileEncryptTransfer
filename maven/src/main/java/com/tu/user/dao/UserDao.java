package com.tu.user.dao;

import java.util.List;
import java.util.Map;


public interface UserDao {
    
	Map<String,Object> selectByPrimaryKey(Integer id);
    
    List<Map<String,Object>> getAll();
    
    int addUser(Map<String,Object> map);
    
    int delete(Map<String,Object> map);
    
    int update(Map<String,Object> map);
}