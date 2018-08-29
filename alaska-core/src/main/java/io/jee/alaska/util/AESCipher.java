package io.jee.alaska.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AESCipher {

	private static final Logger logger = LoggerFactory.getLogger(AESCipher.class);

	private static final String IV_STRING = "A-16-Byte-String";
	private static final String charset = "UTF-8";

	/**
	 * Title: 加密
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param content
	 * @param key
	 * @return
	 * @author XieXiaoXu on 2018年5月18日
	 */
	public static String aesEncryptString(String content, String key) {
		try {
			byte[] contentBytes = content.getBytes(charset);
			byte[] keyBytes = key.getBytes(charset);
			byte[] encryptedBytes = aesEncryptBytes(contentBytes, keyBytes);
			// Encoder encoder = Base64.getEncoder();
			// return encoder.encodeToString(encryptedBytes);
			return Base64.encodeBase64String(encryptedBytes);
		} catch (Exception e) {
			logger.error("AES加密错误");
		}
		return null;
	}

	/**
	 * Title: 解密
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param content
	 * @param key
	 * @return
	 * @author XieXiaoXu on 2018年5月18日
	 */
	public static String aesDecryptString(String content, String key) {
		try {
			// Decoder decoder = Base64.getDecoder();
			// byte[] encryptedBytes = decoder.decode(content);
			byte[] encryptedBytes = Base64.decodeBase64(content);
			byte[] keyBytes = key.getBytes(charset);
			byte[] decryptedBytes = aesDecryptBytes(encryptedBytes, keyBytes);
			return new String(decryptedBytes, charset);
		} catch (Exception e) {
			logger.error("AES解密错误");
		}
		return null;
	}

	/**
	 * Title: 加密
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param contentBytes
	 * @param keyBytes
	 * @return
	 * @author XieXiaoXu on 2018年5月18日
	 */
	public static byte[] aesEncryptBytes(byte[] contentBytes, byte[] keyBytes) {
		try {
			return cipherOperation(contentBytes, keyBytes, Cipher.ENCRYPT_MODE);
		} catch (Exception e) {
			logger.error("AES加密错误");
		}
		return null;
	}

	/**
	 * Title: 解密
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param contentBytes
	 * @param keyBytes
	 * @return
	 * @author XieXiaoXu on 2018年5月18日
	 */
	public static byte[] aesDecryptBytes(byte[] contentBytes, byte[] keyBytes) {
		try {
			return cipherOperation(contentBytes, keyBytes, Cipher.DECRYPT_MODE);
		} catch (Exception e) {
			logger.error("AES解密错误");
		}
		return null;
	}

	private static byte[] cipherOperation(byte[] contentBytes, byte[] keyBytes, int mode) throws Exception {
		SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

		byte[] initParam = IV_STRING.getBytes(charset);
		IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(mode, secretKey, ivParameterSpec);

		return cipher.doFinal(contentBytes);

	}

}