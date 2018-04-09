package com.tu.security.controller;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tu.security.utils.RSACoder;
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
	public @ResponseBody Map<String, Object> key(@RequestBody Map<String, Object> jsonMap) {

		Map<String, Key> map;
		try {
			map = RSACoder.initKey();
			// 生成公钥和私钥
			RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
			RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");
			privateKey.getEncoded();
			// js通过模和公钥指数获取公钥对字符串进行加密，注意必须转为16进制
			// 模
			String modulus = publicKey.getModulus().toString(16);
			// 公钥指数
			String public_exponent = publicKey.getPublicExponent().toString();
			// 私钥指数
			//String private_exponent = privateKey.getPrivateExponent().toString();
			//session.setAttribute("modulus", modulus);
			session.setAttribute("privateKey", privateKey);
			
			String pubKey = modulus + ";" + public_exponent;
			Map<String, Object> key = new HashMap<String, Object>();
			key.put("pubKey", pubKey);
			return key;
		} catch (Exception e) {
			new Exception("获取密钥失败");
			e.printStackTrace();
		}
		return null;
	}
	@RequestMapping("/getAESkey")
	public @ResponseBody Map<String, Object> postAESkey(@RequestBody Map<String, Object> jsonMap) {
		Base64.Encoder encoder = Base64.getEncoder();
		RSAPrivateKey privateKey = (RSAPrivateKey) session.getAttribute("privateKey");
		try {
			String AESkey = RSAUtil.decryptByPrivateKey(jsonMap.get("encrypedPwd").toString(), privateKey);
			System.out.println(AESkey);
			session.setAttribute("AESkey", AESkey);
		} catch (Exception e) {
			new Exception("RSA解密AES对称密钥失败");
			e.printStackTrace();
		}
		return jsonMap;
		
	}
	
}
