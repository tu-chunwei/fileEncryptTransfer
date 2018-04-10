package com.tu.security.utils;

import javax.crypto.Cipher;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lake on 17-4-12.
 */
public class RSACoder {
	public static final String KEY_ALGORITHM = "RSA";
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	private static final String PUBLIC_KEY = "RSAPublicKey";
	private static final String PRIVATE_KEY = "RSAPrivateKey";

	private static Base64.Decoder decoder = Base64.getDecoder();
	private static Base64.Encoder encoder = Base64.getEncoder();

	public static byte[] decryptBASE64(String key) {
		return decoder.decode(key);
	}

	public static String encryptBASE64(byte[] bytes) {
		return encoder.encodeToString(bytes);
	}

	/**
	 * 用私钥对信息生成数字签名
	 *
	 * @param data
	 *            加密数据
	 * @param privateKey
	 *            私钥
	 * @return
	 * @throws Exception
	 */
	public static String sign(byte[] data, String privateKey) throws Exception {
		// 解密由base64编码的私钥
		byte[] keyBytes = decryptBASE64(privateKey);
		// 构造PKCS8EncodedKeySpec对象
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		// KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 取私钥匙对象
		PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// 用私钥对信息生成数字签名
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(priKey);
		signature.update(data);
		return encryptBASE64(signature.sign());
	}

	/**
	 * 校验数字签名
	 *
	 * @param data
	 *            加密数据
	 * @param publicKey
	 *            公钥
	 * @param sign
	 *            数字签名
	 * @return 校验成功返回true 失败返回false
	 * @throws Exception
	 */
	public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
		// 解密由base64编码的公钥
		byte[] keyBytes = decryptBASE64(publicKey);
		// 构造X509EncodedKeySpec对象
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		// KEY_ALGORITHM 指定的加密算法
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 取公钥匙对象
		PublicKey pubKey = keyFactory.generatePublic(keySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(pubKey);
		signature.update(data);
		// 验证签名是否正常
		return signature.verify(decryptBASE64(sign));
	}
	/**
	 * 解密
	 * 用私钥解密
	 *
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String key) throws Exception {
		// 对密钥解密
		byte[] keyBytes = decryptBASE64(key);
		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		// 模长
		int key_len = privateKey.getModulus().bitLength() / 8;
		byte[][] arrays = splitArray(data, key_len);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for (byte[] arr : arrays) {
			byte[] doFinal = cipher.doFinal(arr);
			out.write(doFinal);
		}
		byte[] byteArray = out.toByteArray();
		out.close();
		return byteArray;
	}

	/**
	 * 解密<br>
	 * 用私钥解密
	 *
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPrivateKey(String data, String key) throws Exception {
		return decryptByPrivateKey(decryptBASE64(data), key);
	}

	/**
	 * 解密<br>
	 * 用公钥解密
	 *
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPublicKey(byte[] data, String key) throws Exception {
		// 对密钥解密
		byte[] keyBytes = decryptBASE64(key);
		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		//如果密文长度大于模长则要分组解密 
		int key_len = publicKey.getModulus().bitLength() / 8;
		byte[][] arrays = splitArray(data, key_len);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for (byte[] arr : arrays) {
			byte[] doFinal = cipher.doFinal(arr);
			out.write(doFinal);
		}
		byte[] byteArray = out.toByteArray();
		out.close();
		return byteArray;
	}

	/**
	 * 加密<br>
	 * 用公钥加密
	 *
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPublicKey(String data, String key) throws Exception {
		// 对公钥解密
		byte[] keyBytes = decryptBASE64(key);
		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		int key_len = publicKey.getModulus().bitLength() / 8;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		String[] datas = splitString(data, key_len - 11);
		// 如果明文长度大于模长-11则要分组加密
		for (String s : datas) {
			out.write(cipher.doFinal(s.getBytes()));
		}
		byte[] byteArray = out.toByteArray();
		out.close();
		return byteArray;
		// 对数据加密
	}

	/**
	 * 加密<br>
	 * 用私钥加密
	 *
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String key) throws Exception {
		// 对密钥解密
		byte[] keyBytes = decryptBASE64(key);
		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		int key_len = privateKey.getModulus().bitLength() / 8;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// 加密数据长度 <= 模长-11  
		byte[][] datas = splitArray(data, key_len - 11);
		// 如果明文长度大于模长-11则要分组加密
		for (byte[] s : datas) {
			out.write(cipher.doFinal(s));
		}
		byte[] byteArray = out.toByteArray();
		out.close();
		return byteArray;
	}

	/**
	 * 取得私钥
	 *
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPrivateKey(Map<String, Key> keyMap) throws Exception {
		RSAPrivateKey privateKey = (RSAPrivateKey) keyMap.get(PRIVATE_KEY);
		return encryptBASE64(privateKey.getEncoded());
	}

	/**
	 * 取得公钥
	 *
	 * @param keyMap
	 * @return
	 * @throws Exception
	 */
	public static String getPublicKey(Map<String, Key> keyMap) throws Exception {
		RSAPublicKey publicKey = (RSAPublicKey) keyMap.get(PUBLIC_KEY);
		return encryptBASE64(publicKey.getEncoded());
	}

	/**
	 * 初始化密钥
	 *
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Key> initKey() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		Map<String, Key> keyMap = new HashMap<String, Key>(2);
		keyMap.put(PUBLIC_KEY, keyPair.getPublic());// 公钥
		keyMap.put(PRIVATE_KEY, keyPair.getPrivate());// 私钥
		return keyMap;
	}

	/**
	 * 拆分字符串
	 */
	public static String[] splitString(String string, int len) {
		int x = string.length() / len;
		int y = string.length() % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		String[] strings = new String[x + z];
		String str = "";
		for (int i = 0; i < x + z; i++) {
			if (i == x + z - 1 && y != 0) {
				str = string.substring(i * len, i * len + y);
			} else {
				str = string.substring(i * len, i * len + len);
			}
			strings[i] = str;
		}
		return strings;
	}

	/**
	 * 拆分数组
	 */
	public static byte[][] splitArray(byte[] data, int len) {
		int x = data.length / len;
		int y = data.length % len;
		int z = 0;
		if (y != 0) {
			z = 1;
		}
		byte[][] arrays = new byte[x + z][];
		byte[] arr;
		for (int i = 0; i < x + z; i++) {
			arr = new byte[len];
			if (i == x + z - 1 && y != 0) {
				System.arraycopy(data, i * len, arr, 0, y);
			} else {
				System.arraycopy(data, i * len, arr, 0, len);
			}
			arrays[i] = arr;
		}
		return arrays;
	}

	public static void main(String[] args) throws Exception {
		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYWGgPS2poX8RYeNP+u761YuDiXiPUTx8pvLCSFKhHhyND/i5UMrFs6V50Kc4pGr0yPnjx/MO7MX/14OG5Coq6Znwrbmddapzihvb/blFwKvGdZtcgMPmknAAZNtvRHBSAGIHJxZ4fAci7NLFaJbJGGIq/M1lXv9YEHusji7dW5QIDAQAB";
		String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJhYaA9LamhfxFh40/67vrVi4OJeI9RPHym8sJIUqEeHI0P+LlQysWzpXnQpzikavTI+ePH8w7sxf/Xg4bkKirpmfCtuZ11qnOKG9v9uUXAq8Z1m1yAw+aScABk229EcFIAYgcnFnh8ByLs0sVolskYYir8zWVe/1gQe6yOLt1blAgMBAAECgYBEflLitXx0ysHDKSfe+lgGwxrqi3q+ZIhVQoF8XA9AgXCIjEoWDuiYV9/giD8pWd1ALrg/y+3QRrQKPv4dLLEr5WuwVg9vQrgV9nc+PI4rRfPBw/AVQgF5D9jekRXR4Fux4IPhFbVq0PG7ywberkYdfnE0Pq4qGUrZ+c+bT6vNIQJBAMsgcVKcZIyEedzI78h8dMsL9WADFdUVAasIKSUv+1IpyOiG4vO2xSUNpnbVjI/IgpBFv1vj+s973BvC++7ZUJkCQQDAABgH7vcBxmudLjPlopdoAMQnPKMLwTsu4boZDYs1AOl9nuEAm4Yd/gj/RoqCyIh9mlsPkoyDC6EcyD5qxQwtAkEAlPh2I+AO3f9KYGH9eUvNXBypGIEqwdtmHckeY+eMqfV7Iw3J5HNmooYgT4bOT6T+HFEKxpmsl/5+rt2RZtp6AQJAZlymczRXxD6BCR+k03zHs88UxMZzNsUiRHBbwxeRbAS8dxjJZIWGD19sdIyrM+atvikY+0hqWUNN9gZcIJhFUQJADyNNGd5dJ4X2OMA8i8LB1vMttkap3jx+eeuHXbtKuqfd92J5Zy2t2Pw0EZsKezpHyu/Hd23swPwxrMl0gCv60g==";
		// 产生密钥对
		/*
		 * Map<String, Key> keyMap = RSACoder.initKey(); publicKey =
		 * RSACoder.getPublicKey(keyMap); privateKey = RSACoder.getPrivateKey(keyMap);
		 */
		System.err.println("公钥: \n\r" + publicKey);
		System.err.println("私钥： \n\r" + privateKey);
		System.err.println("公钥加密——私钥解密");
		String receive = "VIBBuDk5E1FmAFjaMKxuLZwgr0VKXELLCP6dl5Av01kpy4VitM3xgMAsx4hdjUC3WD7UMZnSgFipl6EZ6agHou5O+eG+Cb1gZLstTXqyLwZx+ppfbrOHGhncnLcOhpC/aWICos/uWuH82zUe0TmrlD/PEhaJV6lkGSKICPOYtSU=";
		System.out.println("receive" + receive.length());

		String inputStr = "9ad76118-d412-4260-af49-22813c27af74";
		byte[] encodedData = RSACoder.encryptByPublicKey(inputStr, publicKey);
		String encryptBASE64 = encryptBASE64(encodedData);
		byte[] decodedData = RSACoder.decryptByPrivateKey(encryptBASE64, privateKey);
		String outputStr = new String(decodedData, "UTF-8");
		System.err.println("解密后: " + outputStr);
	}
}
