package io.jee.alaska.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AESUtil {

	private static final String IV_STRING = "A-16-Byte-String";
	private static final String charset = "UTF-8";

	/**
	 * 加密
	 * @author xiexx, CAISAN Co.,Ltd.
	 * @version 0.0.1, 2018-09-07 15:08:51
	 *
	 * @param content
	 * @param key
	 * @return
	 */
	public static String encrypt(String content, String key) {
		try {
			byte[] contentBytes = content.getBytes(charset);
			byte[] keyBytes = key.getBytes(charset);
			byte[] encryptedBytes = encrypt(contentBytes, keyBytes);
			return Base64.encodeBase64String(encryptedBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * @author xiexx, CAISAN Co.,Ltd.
	 * @version 0.0.1, 2018-09-07 15:09:00
	 *
	 * @param content
	 * @param key
	 * @return
	 */
	public static String decrypt(String content, String key) {
		try {
			byte[] encryptedBytes = Base64.decodeBase64(content);
			byte[] keyBytes = key.getBytes(charset);
			byte[] decryptedBytes = decrypt(encryptedBytes, keyBytes);
			return new String(decryptedBytes, charset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 加密
	 * @author xiexx, CAISAN Co.,Ltd.
	 * @version 0.0.1, 2018-09-07 15:09:11
	 *
	 * @param contentBytes
	 * @param keyBytes
	 * @return
	 */
	public static byte[] encrypt(byte[] contentBytes, byte[] keyBytes) {
		return cipherOperation(contentBytes, keyBytes, Cipher.ENCRYPT_MODE);
	}

	/**
	 * 解密
	 * @author xiexx, CAISAN Co.,Ltd.
	 * @version 0.0.1, 2018-09-07 15:09:20
	 *
	 * @param contentBytes
	 * @param keyBytes
	 * @return
	 */
	public static byte[] decrypt(byte[] contentBytes, byte[] keyBytes) {
		return cipherOperation(contentBytes, keyBytes, Cipher.DECRYPT_MODE);
	}

	/**
	 * 密码操作
	 * @author xiexx, CAISAN Co.,Ltd.
	 * @version 0.0.1, 2018-09-07 15:10:45
	 *
	 * @param contentBytes
	 * @param keyBytes
	 * @param mode
	 * @return
	 */
	private static byte[] cipherOperation(byte[] contentBytes, byte[] keyBytes, int mode) {
		try {
			SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
			byte[] initParam = IV_STRING.getBytes(charset);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(mode, secretKey, ivParameterSpec);
			return cipher.doFinal(contentBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}