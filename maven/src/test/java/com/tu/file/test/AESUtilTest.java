package com.tu.file.test;

import java.security.GeneralSecurityException;

import com.tu.file.utils.AESUtil;

public class AESUtilTest {
	
	public static final String pwd = "2019";
	
	public static void main(String[] args) {
		try {
			byte[] encrypt = AESUtil.encrypt("423451".getBytes(), pwd);
			byte[] decrypt = AESUtil.decrypt(encrypt, pwd);
			System.out.println(new String(decrypt));
			System.out.println(new String(encrypt));
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
	}
}
