package io.jee.alaska.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

/**
 * <P>Title:简单Date相关静态工具
 * <p>Descriptor: 
 * <P>Copyright (c) CAISAN 2018
 * @author XieXiaoXu on 2018年4月23日
 *
 */
public class DateUtils {

	public static final String CHINESE_DATE_FORMAT_LINE = "yyyy-MM-dd";
	public static final String CHINESE_DATETIME_FORMAT_LINE = "yyyy-MM-dd HH:mm:ss";
	public static final String CHINESE_DATE_FORMAT_SLASH = "yyyy/MM/dd";
	public static final String CHINESE_DATETIME_FORMAT_SLASH = "yyyy/MM/dd HH:mm:ss";
	public static final String DATETIME_NOT_SEPARATOR = "yyyyMMddHHmmssSSS";

	private static final String[] SUPPORT_ALL_FORMATS = new String[] { CHINESE_DATE_FORMAT_LINE,
			CHINESE_DATETIME_FORMAT_LINE, CHINESE_DATE_FORMAT_SLASH, CHINESE_DATETIME_FORMAT_SLASH };

	private static final String DEFAULT_DATE_FORMAT = CHINESE_DATETIME_FORMAT_LINE;
	private static final SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

	private static DateFormat df = null;
	private final static Calendar c = Calendar.getInstance();

	public final static String HH_MM_SS = "HH:mm:ss";
	public final static String YYYY_MM_DD = "yyyy-MM-dd";
	public final static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
	public final static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
	public final static String YYYY_MM_DD_HH_MM_SS_MI = "yyyy-MM-dd HH:mm:ss.S";

	public final static String HHMMSS = "HHmmss";
	public final static String YYYYMMDD = "yyyyMMdd";
	public final static String YYYYMMDDHHMMSS = "yyyyMMdd-HHmmss";

	public static String format(Date date, String pattern) {
		sdf.applyPattern(pattern);
		return sdf.format(date);
	}

	public static String format(Date date) {
		return format(date, DEFAULT_DATE_FORMAT);
	}

	public static String format(String pattern) {
		return format(new Date(), pattern);
	}

	public static Date parse(String dateString, String pattern) {
		sdf.applyPattern(pattern);
		try {
			return sdf.parse(dateString);
		} catch (Exception e) {
			throw new RuntimeException(
					"parse String[" + dateString + "] to Date faulure with pattern[" + pattern + "].");
		}
	}

	public static Date parse(String dateString, String[] patterns) {
		for (String pattern : patterns) {
			if (StringUtils.isBlank(pattern)) {
				continue;
			}
			sdf.applyPattern(pattern);
			try {
				return sdf.parse(dateString);
			} catch (Exception e) {
				// ignore exception
				continue;
			}
		}
		throw new UnsupportedOperationException(
				"Parse String[" + dateString + "] to Date faulure with patterns[" + Arrays.toString(patterns) + "]");

	}

	public static Date parse(String dateString) {
		return parse(dateString, SUPPORT_ALL_FORMATS);
	}

	public static Date addDay(Date date) {
		long oneDayMillisecond = 24 * 60 * 60 * 1000;
		return addDate(date, oneDayMillisecond);
	}

	public static Date minusDay(Date date) {
		long oneDayMillisecond = 24 * 60 * 60 * 1000;
		return addDate(date, -oneDayMillisecond);
	}

	public static Date addDate(Date date, long millisecond) {
		return new Date(date.getTime() + millisecond);
	}

	/**
	 * date to string with format
	 * 
	 * @param d
	 * @param format
	 * @return
	 */
	public static String toString(Date d, String format) {
		if (d != null) {
			df = new SimpleDateFormat(format);
			return df.format(d);
		} else {
			return null;
		}
	}

	/**
	 * @param time
	 * @param format
	 * @return
	 */
	public static String toString(long time, String format) {
		Timestamp t = new Timestamp(time);
		return toString(t, format);
	}

	/**
	 * @param t
	 * @param format
	 * @return
	 */
	public static String toString(Timestamp t, String format) {
		if (t != null) {
			df = new SimpleDateFormat(format);
			return df.format((Date) t);
		} else {
			return null;
		}
	}

	/**
	 * @return
	 */
	public static String today() {
		return toString(new Date(), YYYY_MM_DD);
	}

	/**
	 * 
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static Timestamp toTimestamp(String dateStr, String format) throws IllegalArgumentException {
		if (dateStr != null) {
			df = new SimpleDateFormat(format);
			try {
				Date d = df.parse(dateStr);
				if (d != null) {
					return new Timestamp(d.getTime());
				}
			} catch (ParseException e) {
				throw new IllegalArgumentException(
						":::[toTimestamp]parse [" + dateStr + "] error, format[" + format + "]");
			}
		}
		return null;
	}

	/**
	 * @param dateStr
	 * @param format
	 * @return
	 */
	public static long toLong(String dateStr, String format) {
		Timestamp t = toTimestamp(dateStr, format);
		if (t != null) {
			return t.getTime();
		} else {
			return 0l;
		}
	}

	/**
	 * @return
	 */
	public static Timestamp now() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * @param t
	 * @return
	 */
	public static int getMinute(Timestamp t) {
		if (t == null) {
			return 0;
		}
		synchronized (c) {
			c.setTime(t);
			return c.get(Calendar.MINUTE);
		}
	}

	/**
	 * @param t
	 * @return
	 */
	public static int getHour24(Timestamp t) {
		if (t == null) {
			return 0;
		}
		synchronized (c) {
			c.setTime(t);
			return c.get(Calendar.HOUR_OF_DAY);
		}
	}

	/**
	 * 2010.9.3 11:00:00
	 * 
	 * @return
	 */
	public static Timestamp hourStart() {
		return hourStart(new Date(), 0);
	}

	/**
	 * 2010.9.3 11:59:59
	 * 
	 * @return
	 */
	public static Timestamp hourEnd() {
		return hourEnd(new Date(), 0);
	}

	/**
	 * @param date
	 * @param hoursOffset
	 * @return
	 */
	public static Timestamp hourEnd(Date date, int hoursOffset) {
		synchronized (c) {
			c.setTime(date);
			// ---------------------------------------
			c.add(Calendar.HOUR, hoursOffset);
			// ---------------------------------------
			c.add(Calendar.HOUR_OF_DAY, 1);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			c.add(Calendar.SECOND, -1);
			return new Timestamp(c.getTimeInMillis());
		}
	}

	/**
	 * @param date
	 * @param hoursOffset
	 * @return
	 */
	public static Timestamp hourStart(Date date, int hoursOffset) {
		synchronized (c) {
			c.setTime(date);
			// ---------------------------------------
			c.add(Calendar.HOUR, hoursOffset);
			// ---------------------------------------
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return new Timestamp(c.getTimeInMillis());
		}
	}

	/**
	 * @param date
	 * @return
	 */
	public static Timestamp dayStart(Date date) {
		return dayStart(date, 0);
	}

	/**
	 * @param date
	 * @param dayOffset
	 * @return
	 */
	public static Timestamp dayStart(Date date, int dayOffset) {
		synchronized (c) {
			c.setTime(date);
			// ---------------------------------------
			c.add(Calendar.DAY_OF_YEAR, dayOffset);
			// ---------------------------------------
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return new Timestamp(c.getTimeInMillis());
		}
	}

	/**
	 * @param date
	 * @return
	 */
	public static Timestamp dayEnd(Date date, int dayOffset) {
		synchronized (c) {
			c.setTime(date);
			// ---------------------------------------
			c.add(Calendar.DAY_OF_YEAR, dayOffset);
			// ---------------------------------------
			c.add(Calendar.DAY_OF_YEAR, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			c.add(Calendar.SECOND, -1);
			return new Timestamp(c.getTimeInMillis());
		}
	}

	/**
	 * @param date
	 * @return
	 */
	public static Timestamp dayEnd(Date date) {
		return dayEnd(date, 0);
	}

	/**
	 * 2010.9.3 00:00:00
	 * 
	 * @return
	 */
	public static Timestamp todayStart() {
		return dayStart(new Date());
	}

	/**
	 * 2010.9.3 23:59:59
	 * 
	 * @return
	 */
	public static Timestamp todayEnd() {
		return dayEnd(new Date());
	}

	/**
	 * @param date
	 * @param dayOffset
	 * @return
	 */
	public static Timestamp monthStart(Date date) {
		return monthStart(date, 0);
	}

	/**
	 * @param date
	 * @return
	 */
	public static Timestamp monthStart(Date date, int dayOffset) {
		synchronized (c) {
			c.setTime(date);
			// -----------------------------------
			c.add(Calendar.DAY_OF_YEAR, dayOffset);
			// -----------------------------------
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return new Timestamp(c.getTimeInMillis());
		}
	}

	/**
	 * @param date
	 * @param dayOffset
	 * @return
	 */
	public static Timestamp monthEnd(Date date) {
		return monthEnd(date, 0);
	}

	/**
	 * 2010.10.3 -> 2010.11.1 -> 2010.10.31
	 * 
	 * @param date
	 * @return
	 */
	public static Timestamp monthEnd(Date date, int dayOffset) {
		synchronized (c) {
			c.setTime(date);
			// -----------------------------------
			c.add(Calendar.DAY_OF_YEAR, dayOffset);
			// -----------------------------------
			c.add(Calendar.MONTH, 1);
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			c.add(Calendar.SECOND, -1);
			// ------------------------------------

			// ------------------------------------
			return new Timestamp(c.getTimeInMillis());
		}
	}

	/**
	 * 2010.9.1 00:00:00
	 * 
	 * @return
	 */
	public static Timestamp monthStart() {
		return monthStart(new Date());
	}

	/**
	 * 2010.9.30 23:59:59
	 * 
	 * @return
	 */
	public static Timestamp monthEnd() {
		return monthEnd(new Date());
	}

	/**
	 * @param date
	 * @return
	 */
	public static Timestamp yearStart(Date date) {
		return yearStart(date, 0);
	}

	/**
	 * @param date
	 * @return
	 */
	public static Timestamp yearStart(Date date, int dayOffset) {
		synchronized (c) {
			c.setTime(date);
			// -----------------------------------
			c.add(Calendar.DAY_OF_YEAR, dayOffset);
			// -----------------------------------
			c.set(Calendar.MONTH, 0);
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			return new Timestamp(c.getTimeInMillis());
		}

	}

	/**
	 * @param date
	 * @return
	 */
	public static Timestamp yearEnd(Date date) {
		return yearEnd(date, 0);
	}

	/**
	 * @param date
	 * @param dayOffset
	 * @return
	 */
	public static Timestamp yearEnd(Date date, int dayOffset) {
		synchronized (c) {
			c.setTime(date);
			// -----------------------------------
			c.add(Calendar.DAY_OF_YEAR, dayOffset);
			// -----------------------------------
			c.add(Calendar.YEAR, 1);
			c.set(Calendar.MONTH, 0);
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			c.add(Calendar.SECOND, -1);
			return new Timestamp(c.getTimeInMillis());
		}

	}

	/**
	 * 
	 * @return
	 */
	public static Timestamp yearStart() {
		return yearStart(new Date());
	}

	/**
	 * @return
	 */
	public static Timestamp yearEnd() {
		return yearEnd(new Date());
	}

	/**
	 * @param timestamp
	 * @param field
	 * @param value
	 * @return
	 */
	public static Date add(Date timestamp, int field, int value) {
		synchronized (c) {
			c.setTimeInMillis(timestamp.getTime());
			c.add(field, value);
			return new Date(c.getTimeInMillis());
		}

	}

	/**
	 * 
	 * @param date
	 * @param value
	 *            n (y:years)(m:months)|(d:days)|(h:hours)|(mi:minutes)|(s:second)
	 * @return
	 */
	public static Date add(Date date, String value) {
		int n;
		String unit;
		try {
			n = RegexUtils.extractInteger(value, "");
			unit = value.replaceAll(String.valueOf(n), "");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("invalid format :" + value);
		}

		synchronized (c) {
			c.setTime(date);
			if (unit.toLowerCase().startsWith("y")) {
				c.add(Calendar.YEAR, n);
			} else if (unit.toLowerCase().startsWith("mi")) {
				c.add(Calendar.MINUTE, n);
			} else if (unit.toLowerCase().startsWith("m")) {
				c.add(Calendar.MONTH, n);
			} else if (unit.toLowerCase().startsWith("d")) {
				c.add(Calendar.DATE, n);
			} else if (unit.toLowerCase().startsWith("h")) {
				c.add(Calendar.HOUR, n);
			} else if (unit.toLowerCase().startsWith("s")) {
				c.add(Calendar.SECOND, n);
			}
			return new Timestamp(c.getTimeInMillis());
		}
	}

	/**
	 * 
	 * @param date
	 * @param value
	 *            n (y:years)(m:months)|(d:days)|(h:hours)|(mi:minutes)|(s:second)
	 * @return
	 */
	public static Date reduce(Date date, String value) {
		int n;
		String unit;
		try {
			n = RegexUtils.extractInteger(value, "");
			unit = value.replaceAll(String.valueOf(n), "");
			n = n * -1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("invalid format :" + value);
		}

		synchronized (c) {
			c.setTime(date);
			if (unit.toLowerCase().startsWith("y")) {
				c.add(Calendar.YEAR, n);
			} else if (unit.toLowerCase().startsWith("mi")) {
				c.add(Calendar.MINUTE, n);
			} else if (unit.toLowerCase().startsWith("m")) {
				c.add(Calendar.MONTH, n);
			} else if (unit.toLowerCase().startsWith("d")) {
				c.add(Calendar.DATE, n);
			} else if (unit.toLowerCase().startsWith("h")) {
				c.add(Calendar.HOUR, n);
			} else if (unit.toLowerCase().startsWith("s")) {
				c.add(Calendar.SECOND, n);
			}
			return new Timestamp(c.getTimeInMillis());
		}
	}

	/**
	 * @param timestamp
	 * @param field
	 * @return
	 */
	public static int get(Date timestamp, int field) {
		synchronized (c) {
			c.setTimeInMillis(timestamp.getTime());
			return c.get(field);
		}
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	public static int getMonthDays(Date date) {
		// 1st of next month -1s
		int dayOffset = 0;
		synchronized (c) {
			c.setTime(date);
			// -----------------------------------
			c.add(Calendar.DAY_OF_YEAR, dayOffset);
			// -----------------------------------
			c.add(Calendar.MONTH, 1);
			c.set(Calendar.DAY_OF_MONTH, 1);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			c.add(Calendar.SECOND, -1);
			// ------------------------------------

			return c.get(Calendar.DAY_OF_MONTH);
		}
	}

	/**
	 * @param serverTime
	 * @param serverZT
	 * @param clientZT
	 * @return
	 */
	public static long getClientTime(long serverTime, TimeZone serverZT, TimeZone clientZT) {
		return serverTime + clientZT.getRawOffset() - serverZT.getRawOffset();
	}

	/**
	 * 1 - sunday 2 - monday ..
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekDay(Date date) {
		synchronized (c) {
			c.setTime(date);
			return c.get(Calendar.DAY_OF_WEEK);
		}
	}

	/**
	 * 
	 * @param milliseconds
	 * @return xx天xx小时xx分钟xx秒
	 */
	public static String toTimeString(Long milliseconds) {
		if (milliseconds < 1000l) {
			return "0";
		}

		long day = milliseconds / (24 * 60 * 60 * 1000);
		long hour = (milliseconds / (60 * 60 * 1000) - day * 24);
		long min = ((milliseconds / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (milliseconds / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

		StringBuffer sb = new StringBuffer();
		if (day > 0) {
			sb.append(day);
			sb.append("天");
		}
		if (hour > 0 || (day > 0 && (min > 0 || s > 0))) {
			sb.append(hour);
			sb.append("小时");
		}
		if (min > 0 || ((day > 0 || hour > 0) && s > 0)) {
			sb.append(min);
			sb.append("分");
		}
		if (s > 0) {
			sb.append(s);
			sb.append("秒");
		}
		return sb.toString();
	}
	
    /**
	 * 由过去的某一时间,计算距离当前的时间
	 */
	public static String pastTime(Date setTime) {
		if(setTime==null) {
			return "";
		}
		long nowTime = System.currentTimeMillis(); // 获取当前时间的毫秒数
		String msg = null;
		long reset = setTime.getTime(); // 获取指定时间的毫秒数
		long dateDiff = nowTime - reset;
		if (dateDiff < 0) {
			msg = "date error";
		} else {
			long dateTemp1 = dateDiff / 1000; // 秒
			long dateTemp2 = dateTemp1 / 60; // 分钟
			long dateTemp3 = dateTemp2 / 60; // 小时
			long dateTemp4 = dateTemp3 / 24; // 天数
			long dateTemp5 = dateTemp4 / 30; // 月数
			long dateTemp6 = dateTemp5 / 12; // 年数
			if (dateTemp6 > 0) {
				msg = dateTemp6 + " years ago";
			} else if (dateTemp5 > 0) {
				msg = dateTemp5 + " months ago";
			} else if (dateTemp4 > 0) {
				msg = dateTemp4 + " days ago";
			} else if (dateTemp3 > 0) {
				msg = dateTemp3 + " hours ago";
			} else if (dateTemp2 > 0) {
				msg = dateTemp2 + " minutes ago";
			} else if (dateTemp1 > 0) {
				msg = "just now";
			}
		}
		return msg;
	}

}
