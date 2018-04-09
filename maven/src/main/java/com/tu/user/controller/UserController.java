package com.tu.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tu.user.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Resource
	public UserService userService;
	@Resource
	private HttpSession session;
	
	@RequestMapping("/home")
	private ModelAndView init(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		List<Map<String, Object>> user = userService.getAll();
		mv.addObject("user", user);
		mv.setViewName("index");
		return mv;
	}
	@RequestMapping("/toRegister")
	private ModelAndView toRegister(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("register");
		return mv;
	}
	@RequestMapping("/toLogin")
	private ModelAndView toLogin(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("login");
		return mv;
	}
	@RequestMapping("/index/load")
	private @ResponseBody List<Map<String, Object>> requestJson(@RequestBody Map<String, Object> map) {
		List<Map<String, Object>> list = userService.getAll();
		return list;
	}

	@RequestMapping("/register")
	private @ResponseBody Map<String, Object> register(@RequestBody Map<String, Object> map) {
		session.setAttribute("name", "tu");
		return userService.addUser(map);
	}

	@RequestMapping("/deleteuser")
	private @ResponseBody Map<String, Object> delete(@RequestBody Map<String, Object> map) {
		int num = userService.delete(map);
		Map<String, Object> state = new HashMap<String, Object>();
		state.put("result", num);
		return state;
	}

	@RequestMapping("/update")
	private @ResponseBody Map<String, Object> update(@RequestBody Map<String, Object> map) {
		int num = userService.update(map);
		Map<String, Object> state = new HashMap<String, Object>();
		state.put("result", num);
		return state;
	}

}
