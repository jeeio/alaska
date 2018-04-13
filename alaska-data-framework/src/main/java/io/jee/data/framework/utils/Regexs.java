package io.jee.data.framework.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wanghailong 
 */
public class Regexs {
	public static void main(String args[]){
		System.out.println(Regexs.matcher(DATE_YYYY_MM_DD_REGEXP, "2233-32-22"));
		System.out.println(Regexs.matcher(DATE_YYYY_MM_DD_HH_MM_SS_REGEXP, "2233-32-22 11:33:44"));
	}
	
	//yyyy-mm-dd hh:mm:ss正则表表达试，只验证格式
	public static final String DATE_YYYY_MM_DD_HH_MM_SS_REGEXP = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$";
	//yyyy-mm-dd hh:mm:ss正则表表达试，只验证格式
	public static final String DATE_YYYY_MM_DD_HH_MM_REGEXP = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$";
	//yyyy-mm-dd正则表表达试，只验证格式
	public static final String DATE_YYYY_MM_DD_REGEXP = "^\\d{4}-\\d{2}-\\d{2}$";
	
	public static String extract(String regExp,String content)
	{
        Pattern p=Pattern.compile(regExp);
        Matcher m=p.matcher(content);
        if(m.find())
        {
            return m.group();
        }
        else 
        {
        	return null;
        }
	}

	public static  boolean matcher(String regExp,String content)
	{
		 Pattern p=Pattern.compile(regExp);
         Matcher m=p.matcher(content);
         return m.matches();
	}
	
	public static Integer extractInteger(String content,String prefix)
	{
		String numberString = extractNumber(content,prefix);
		if(numberString == null)
		{
			return null;
		}
		
		return Integer.valueOf(numberString);
	}
	
	public static Long extractLong(String content,String prefix)
	{
		String numberString = extractNumber(content,prefix);
		if(numberString == null)
		{
			return null;
		}
		
		return Long.valueOf(numberString);
	}
	
	private static String extractNumber(String content,String prefix)
	{
		if(content ==null)
		{
			return null;
		}
		try
		{
			if(prefix!=null){
				return Regexs.extract(prefix+"\\s*\\d+", content).replaceAll(prefix, "").trim();
			}else{
				return Regexs.extract("\\d+", content);
			}
			
		} catch (Exception e) {
			return null;
		}
	}
}
