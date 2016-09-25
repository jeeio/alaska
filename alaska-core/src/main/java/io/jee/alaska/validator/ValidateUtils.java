package io.jee.alaska.validator;

import java.util.regex.Pattern;

public class ValidateUtils {
	
	public static boolean validDomain(String domain){
		return valid("^[0-9a-zA-Z]+[0-9a-zA-Z\\.-]*\\.[a-zA-Z]{2,8}$", domain);
	}
	
	/**
	 * 不带www.开头 
	 * @param rootDomain
	 * @return
	 */
	public static boolean validRootDomain(String rootDomain){
		return valid("^(?!www\\.)[0-9a-zA-Z]+[0-9a-zA-Z\\.-]*\\.[a-zA-Z]{2,8}$", rootDomain);
	}
	
	public static boolean validIp(String ip){
		return valid("(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\."
				+ "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\."
				+ "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\."
				+ "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)", ip);
	}

	public static boolean validMobile(String mobile){
		return valid("^1\\d{10}$", mobile);
	}
	
	public static boolean validEmail(String email){
		return valid("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*", email);
	}
	
	public static boolean valid(String regex, String input){
		if(input==null){
			return false;
		}
		return Pattern.matches(regex, input);
	}
	
}
