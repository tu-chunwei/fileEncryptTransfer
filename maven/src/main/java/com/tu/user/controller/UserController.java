package com.tu.user.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.tu.file.utils.Base64;
import com.tu.file.utils.RSAUtil;
import com.tu.user.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

	@Resource
	private UserService userService;

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
	/**
	 * 请求公钥
	 * 
	 * @param map
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("/randomShock")
	private @ResponseBody Map<String, Object> randomShock(@RequestBody Map<String, Object> map1) throws UnsupportedEncodingException{

		Map<String, Object> map;
		try {
			map = RSAUtil.getKeys();
			// 生成公钥和私钥
			RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
			RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");

			// 模
			String modulus = publicKey.getModulus().toString();
			// 公钥指数
			String public_exponent = publicKey.getPublicExponent().toString();
			// 私钥指数
			String private_exponent = privateKey.getPrivateExponent().toString();
			// 明文
			// 使用模和指数生成公钥和私钥
			RSAPublicKey pubKey = RSAUtil.getPublicKey(modulus, public_exponent);
			RSAPrivateKey priKey = RSAUtil.getPrivateKey(modulus, private_exponent);
			/*byte[] priKeyEncoded= priKey.getEncoded();
			byte[] pubKeyEncoded = pubKey.getEncoded();
			try {
				System.out.println(new String(Base64.encode(pubKeyEncoded),"UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			Map<String, Object> key = new HashMap<String, Object>();
			key.put("pubKey", pubKey);
			return key;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	@RequestMapping("/index/load")
	private @ResponseBody List<Map<String, Object>> requestJson(@RequestBody Map<String, Object> map) {
		List<Map<String, Object>> list = userService.getAll();
		return list;
	}

	@RequestMapping("/register")
	private @ResponseBody Map<String, Object> register(@RequestBody Map<String, Object> map) {
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
