package com.tu.security.controller;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tu.security.utils.RSAUtil;

@Controller
@RequestMapping("/security")
public class SecurityController {
	@Resource
	private HttpSession session;

	/**
	 * 请求公钥
	 * 
	 * @param map
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping("/key")
	private @ResponseBody Map<String, Object> randomShock(@RequestBody Map<String, Object> map1) {

		Map<String, Object> map;
		try {
			map = RSAUtil.getKeys();
			// 生成公钥和私钥
			RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
			RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");
			// js通过模和公钥指数获取公钥对字符串进行加密，注意必须转为16进制
			// 模
			String modulus = publicKey.getModulus().toString(16);
			// 公钥指数
			String public_exponent = publicKey.getPublicExponent().toString();
			// 私钥指数
			String private_exponent = privateKey.getPrivateExponent().toString();
			session.setAttribute("modulus", modulus);
			session.setAttribute(private_exponent, private_exponent);
			String pubKey = modulus + ";" + public_exponent;
			Map<String, Object> key = new HashMap<String, Object>();
			key.put("pubKey", pubKey);
			return key;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
