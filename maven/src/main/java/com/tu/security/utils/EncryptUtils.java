package com.tu.security.utils;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import org.springframework.util.StringUtils;

/**
 * 编码工具类 实现aes加密、解密
 */
public class EncryptUtils {

	/**
	 * 密钥
	 */
	// key支持128位，192位，256位，所以密钥为16位/24位/32位,去除该限制-https://blog.csdn.net/wangjunjun2008/article/details/50847426
	private static final String KEY = "97e6bc8d-dbf2-5c08-9ca2-2b69c07e";
	private static Base64.Decoder decoder = Base64.getDecoder();
	private static Base64.Encoder encoder = Base64.getEncoder();

	/**
	 * 算法
	 */
	private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

	public static void main(String[] args) throws Exception {
		String content = "我爱你";
		System.out.println("加密前：" + content);

		System.out.println("加密密钥和解密密钥：" + KEY);

		String encrypt = aesEncrypt(content, KEY);
		System.out.println("加密后：" + encrypt);

		String decrypt = aesDecrypt(encrypt, KEY);
		System.out.println("解密后：" + decrypt);
	}

	/**
	 * aes解密
	 * 
	 * @param encrypt
	 *            内容
	 * @return
	 * @throws Exception
	 */
	public static String aesDecrypt(String encrypt) throws Exception {
		return aesDecrypt(encrypt, KEY);
	}

	/**
	 * aes加密
	 * 
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static String aesEncrypt(String content) throws Exception {
		return aesEncrypt(content, KEY);
	}

	/**
	 * 将byte[]转为各种进制的字符串
	 * 
	 * @param bytes
	 *            byte[]
	 * @param radix
	 *            可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
	 * @return 转换后的字符串
	 */
	public static String binary(byte[] bytes, int radix) {
		return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
	}

	/**
	 * base 64 encode
	 * 
	 * @param bytes
	 *            待编码的byte[]
	 * @return 编码后的base 64 code
	 */
	public static String base64Encode(byte[] bytes) {
		return encoder.encodeToString(bytes);
	}

	/**
	 * base 64 decode
	 * 
	 * @param base64Code
	 *            待解码的base 64 code
	 * @return 解码后的byte[]
	 * @throws Exception
	 */
	public static byte[] base64Decode(String base64Code) throws Exception {
		// Base64.Decoder decoder = Base64.getDecoder();
		return StringUtils.isEmpty(base64Code) ? null : decoder.decode(base64Code);
	}

	/**
	 * AES加密
	 * 
	 * @param content
	 *            待加密的内容
	 * @param encryptKey
	 *            加密密钥
	 * @return 加密后的byte[]
	 * @throws Exception
	 */
	public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
		/*KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128);*/
		Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
		System.out.println(encryptKey.getBytes());
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(encryptKey.getBytes(), "AES"));

		return cipher.doFinal(content.getBytes("utf-8"));
	}

	/**
	 * AES加密为base 64 code
	 * 
	 * @param content
	 *            待加密的内容
	 * @param encryptKey
	 *            加密密钥
	 * @return 加密后的base 64 code
	 * @throws Exception
	 */
	public static String aesEncrypt(String content, String encryptKey) throws Exception {
		return base64Encode(aesEncryptToBytes(content, encryptKey));
	}

	/**
	 * AES解密
	 * 
	 * @param encryptBytes
	 *            待解密的byte[]
	 * @param decryptKey
	 *            解密密钥
	 * @return 解密后的String
	 * @throws Exception
	 */
	public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
		/*KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(decryptKey.getBytes());
		kgen.init(128, secureRandom);
*/
		Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(decryptKey.getBytes(), "AES"));
		byte[] decryptBytes = cipher.doFinal(encryptBytes);

		return new String(decryptBytes);
	}
	/**
	 * 生成密钥
	 * @param password 字符串，种子
	 * @return
	 * @throws NoSuchAlgorithmException 
	 */
	public static void generateKey(String decryptKey) throws NoSuchAlgorithmException {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(decryptKey.getBytes());
		kgen.init(128, secureRandom);
	}
	/**
	 * 将base 64 code AES解密
	 * 
	 * @param encryptStr
	 *            待解密的base 64 code
	 * @param decryptKey
	 *            解密密钥
	 * @return 解密后的string
	 * @throws Exception
	 */
	public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
		return StringUtils.isEmpty(encryptStr) ? null : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
	}

}