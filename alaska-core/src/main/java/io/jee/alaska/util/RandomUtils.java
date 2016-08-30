package io.jee.alaska.util;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;

public class RandomUtils {
	
	public final static String ALL = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public final static String NUMBER = "0123456789";
	public final static String UPPERCASS_AND_NUMBER = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public final static String LOWERCASS = "abcdefghijklmnopqrstuvwxyz";
	
	public static String getRandom(String base, int length){
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString(); 
	}
	
	public static String getLetterAndNumber(int length){
		String result = getRandom(ALL, length);
		while (StringUtils.isAlpha(result)||StringUtils.isNumeric(result)) {
			result = getRandom(ALL, length);
		}
		return result;
	}
	
	public static String getLetter(int length){
		String result = getRandom(LOWERCASS, length);
		return result;
	}
	
	public static String getAll(int length) {
		return getRandom(ALL, length);
	}
	
	public static String getNumber(int length) { // length表示生成字符串的长度
		return getRandom(NUMBER, length);
	}
	
	public static String getNumberFristNotZero(int length){
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		boolean frist = true;
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(NUMBER.length());
			char f = NUMBER.charAt(number);
			if(frist){
				while(f == '0'){
					f = NUMBER.charAt(number);
				}
				frist = false;
			}
			sb.append(f);
		}
		return sb.toString(); 
	}

}
