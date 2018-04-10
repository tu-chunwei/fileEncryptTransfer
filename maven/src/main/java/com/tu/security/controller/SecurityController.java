package com.tu.security.controller;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tu.security.utils.EncryptUtils;
import com.tu.security.utils.RSACoder;

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

		Map<String, Key> keyMap;
		try {
			keyMap = RSACoder.initKey();
			// 生成公钥和私钥
			String privateKey = RSACoder.getPrivateKey(keyMap);
			String publicKey = RSACoder.getPublicKey(keyMap);
			
			
			/*RSAPublicKey publicKey = (RSAPublicKey) map.get("public");
			RSAPrivateKey privateKey = (RSAPrivateKey) map.get("private");*/
			/*privateKey.getEncoded();*/
			// js通过模和公钥指数获取公钥对字符串进行加密，注意必须转为16进制
			// 模
			/*String modulus = publicKey.getModulus().toString(16);
			// 公钥指数
			String public_exponent = publicKey.getPublicExponent().toString();*/
			// 私钥指数
			//String private_exponent = privateKey.getPrivateExponent().toString();
			//session.setAttribute("modulus", modulus);
			session.setAttribute("priKey", privateKey);
			
			/*String pubKey = modulus + ";" + public_exponent;*/
			Map<String, Object> key = new HashMap<String, Object>();
			key.put("pubKey", publicKey);
			return key;
		} catch (Exception e) {
			new Exception("获取密钥失败");
			e.printStackTrace();
		}
		return null;
	}
	@RequestMapping("/postAESkey")
	public @ResponseBody Map<String, Object> postAESkey(@RequestBody Map<String, Object> map, HttpServletResponse response) {
		String privateKey = (String) session.getAttribute("priKey");
		try {
			byte[] AESkey = RSACoder.decryptByPrivateKey(map.get("encrypedPwd").toString(), privateKey);
			System.err.print("AESKey:");
			System.out.println(new String(AESkey,"UTF-8"));
			session.setAttribute("AESkey", new String(AESkey,"UTF-8"));
			Cookie cookie = new Cookie("AESkey","AESkey");
			cookie.setMaxAge(30*60);
			cookie.setPath("/");
			response.addCookie(cookie);
		} catch (Exception e) {
			new Exception("RSA解密AES对称密钥失败");
			e.printStackTrace();
		}
		return map;
	}
	@RequestMapping("/AESTest")
	public @ResponseBody Map<String, Object> AESTest(@RequestBody Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		String a = map.get("text").toString();
		String AESkey = session.getAttribute("AESkey").toString();
		try {
			String aesDecrypt = EncryptUtils.aesDecrypt(a, AESkey);
			result.put("decodeResult", aesDecrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		/*String privateKey = (String) session.getAttribute("priKey");
		try {
			byte[] AESkey = RSACoder.decryptByPrivateKey(map.get("encrypedPwd").toString(), privateKey);
			System.err.print("AESKey:");
			System.out.println(new String(AESkey,"UTF-8"));
			session.setAttribute("AESkey", AESkey);
			Cookie cookie = new Cookie("AESkey","AESkey");
			cookie.setMaxAge(30*60);
			cookie.setPath("/");
			response.addCookie(cookie);
		} catch (Exception e) {
			new Exception("RSA解密AES对称密钥失败");
			e.printStackTrace();
		}
		return map;*/
	}
}
