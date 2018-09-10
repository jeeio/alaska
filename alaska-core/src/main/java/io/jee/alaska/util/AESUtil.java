package io.jee.alaska.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
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
	 * @throws UnsupportedEncodingException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static String encrypt(String content, String key) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		byte[] contentBytes = content.getBytes(charset);
		byte[] keyBytes = key.getBytes(charset);
		byte[] encryptedBytes = encrypt(contentBytes, keyBytes);
		return Base64.encodeBase64String(encryptedBytes);
	}

	/**
	 * 解密
	 * @author xiexx, CAISAN Co.,Ltd.
	 * @version 0.0.1, 2018-09-07 15:09:00
	 *
	 * @param content
	 * @param key
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws InvalidKeyException 
	 */
	public static String decrypt(String content, String key) throws UnsupportedEncodingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		byte[] encryptedBytes = Base64.decodeBase64(content);
		byte[] keyBytes = key.getBytes(charset);
		byte[] decryptedBytes = decrypt(encryptedBytes, keyBytes);
		return new String(decryptedBytes, charset);
	}

	/**
	 * 加密
	 * @author xiexx, CAISAN Co.,Ltd.
	 * @version 0.0.1, 2018-09-07 15:09:11
	 *
	 * @param contentBytes
	 * @param keyBytes
	 * @return
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 */
	public static byte[] encrypt(byte[] contentBytes, byte[] keyBytes) throws IllegalBlockSizeException, BadPaddingException {
		return cipherOperation(contentBytes, keyBytes, Cipher.ENCRYPT_MODE);
	}

	/**
	 * 解密
	 * @author xiexx, CAISAN Co.,Ltd.
	 * @version 0.0.1, 2018-09-10 15:04:11
	 *
	 * @param contentBytes
	 * @param keyBytes
	 * @return
	 * @throws BadPaddingException 
	 * @throws IllegalBlockSizeException 
	 * @throws InvalidAlgorithmParameterException 
	 * @throws InvalidKeyException 
	 */
	public static byte[] decrypt(byte[] contentBytes, byte[] keyBytes) throws InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		return cipherOperation(contentBytes, keyBytes, Cipher.DECRYPT_MODE);
	}

	/**
	 * 密码操作
	 * @author xiexx, CAISAN Co.,Ltd.
	 * @version 0.0.1, 2018-09-10 15:03:29
	 *
	 * @param contentBytes
	 * @param keyBytes
	 * @param mode
	 * @return
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	private static byte[] cipherOperation(byte[] contentBytes, byte[] keyBytes, int mode) throws IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
		Cipher cipher = null;
		try {
			byte[] initParam = IV_STRING.getBytes(charset);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(initParam);
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(mode, secretKey, ivParameterSpec);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		}
		return cipher.doFinal(contentBytes);
	}

}