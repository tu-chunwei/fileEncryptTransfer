package com.tu.security.utils;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
	
	private static final String ALGORITHM = "AES";//算法
	
	/**
	 * 加密
	 * @param content 需要加密的内容
	 * @param password 加密密码
	 * @return
	 */
	public static byte[] encrypt(byte[] content, String password) throws GeneralSecurityException {
		SecretKeySpec key = getKeyByPass(password);
		Cipher cipher = Cipher.getInstance(ALGORITHM);// 创建密码器
		//byte[] byteContent = content.getBytes();
		cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
		byte[] result = cipher.doFinal(content);
		return result; // 加密
	}
	
	/**
	 * 解密
	 * @param content 待解密内容
	 * @param password 解密密钥
	 * @return
	 */
	public static byte[] decrypt(byte[] content, String password) throws GeneralSecurityException {
		SecretKeySpec key = getKeyByPass(password);
		Cipher cipher = Cipher.getInstance(ALGORITHM);// 创建密码器
		cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
		byte[] result = cipher.doFinal(content);
		return result; // 解密
	}
	
	/**
	 * 生成密钥
	 * @param password 字符串，种子
	 * @return
	 */
	public static SecretKeySpec getKeyByPass(String password) {
		KeyGenerator kgen;
		try {
			kgen = KeyGenerator.getInstance(ALGORITHM);
			SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
			//种子，种子同，序列就一样，生成密钥就一样
	        secureRandom.setSeed(password.getBytes());
	        //生成密钥位数，128,192,256
			kgen.init(128, secureRandom);
			//安全随机数序列
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, ALGORITHM);
			System.out.println(key);
			return key;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
}
