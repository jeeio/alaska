package io.jee.alaska.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <P>Title:正则表表达试工具类
 * <p>Descriptor: 
 * <P>Copyright (c) CAISAN 2018
 * @author XieXiaoXu on 2018年4月23日
 *
 */
public class Regexs {
	// yyyy-mm-dd hh:mm:ss正则表表达试，只验证格式
	public static final String DATE_YYYY_MM_DD_HH_MM_SS_REGEXP = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
	// yyyy-mm-dd hh:mm:ss正则表表达试，只验证格式
	public static final String DATE_YYYY_MM_DD_HH_MM_REGEXP = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$";
	// yyyy-mm-dd正则表表达试，只验证格式
	public static final String DATE_YYYY_MM_DD_REGEXP = "^\\d{4}-\\d{2}-\\d{2}$";

	public static String extract(String regExp, String content) {
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(content);
		if (m.find()) {
			return m.group();
		} else {
			return null;
		}
	}

	public static boolean matcher(String regExp, String content) {
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(content);
		return m.matches();
	}

	public static Integer extractInteger(String content, String prefix) {
		String numberString = extractNumber(content, prefix);
		if (numberString == null) {
			return null;
		}

		return Integer.valueOf(numberString);
	}

	public static Long extractLong(String content, String prefix) {
		String numberString = extractNumber(content, prefix);
		if (numberString == null) {
			return null;
		}

		return Long.valueOf(numberString);
	}

	private static String extractNumber(String content, String prefix) {
		if (content == null) {
			return null;
		}
		try {
			if (prefix != null) {
				return Regexs.extract(prefix + "\\s*\\d+", content).replaceAll(prefix, "").trim();
			} else {
				return Regexs.extract("\\d+", content);
			}

		} catch (Exception e) {
			return null;
		}
	}
}
