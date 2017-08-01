package io.jee.alaska.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {
	
	public static boolean isSameDate(Date one, Date two) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(one);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(two);
		
		boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
		           .get(Calendar.YEAR);
		boolean isSameMonth = isSameYear
		           && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
		boolean isSameDate = isSameMonth
		           && cal1.get(Calendar.DAY_OF_MONTH) == cal2
		                   .get(Calendar.DAY_OF_MONTH);
		return isSameDate;
	}

	public static BigDecimal dayDiff(Date start, Date end) {
		Calendar starCal = Calendar.getInstance();
		starCal.setTime(start);

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(end);
		
		long milliseconds1 = starCal.getTimeInMillis();
		long milliseconds2 = endCal.getTimeInMillis();
		long diff = milliseconds2 - milliseconds1;
		
		BigDecimal num = new BigDecimal(diff).divide(new BigDecimal(24 * 60 * 60 * 1000), 5, BigDecimal.ROUND_HALF_UP);

		return num;

	}
	
	public static int secondDiff(Date start, Date end) {
		Calendar starCal = Calendar.getInstance();
		starCal.setTime(start);

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(end);
		
		long milliseconds1 = starCal.getTimeInMillis();
		long milliseconds2 = endCal.getTimeInMillis();
		int diff = (int) ((milliseconds2 - milliseconds1)/1000);
		return diff;
	}
	
	public static Date addYear(Date date, int year) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, year);
		return cal.getTime();
	}
	
	public static Date addYear(int year) {
		return addYear(new Date(), year);
	}
	
	public static Date addMonth(Date date, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, month);
		return cal.getTime();
	}
	
	public static Date addMonth(int month) {
		return addMonth(new Date(), month);
	}

	public static Date addDay(Date date, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}
	
	public static Date addHour(Date date, int hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, hour);
		return cal.getTime();
	}
	
	public static Date addHour(int hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR, hour);
		return cal.getTime();
	}
	
	public static Date addSecond(Date date, int second) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, second);
		return cal.getTime();
	}
	
	public static Date addSecond(int second) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.SECOND, second);
		return cal.getTime();
	}
	
	public static Date addMinute(Date date, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minute);
		return cal.getTime();
	}
	
	public static Date addMinute(int minute) {
		return addMinute(new Date(), minute);
	}

	public static Date addDay(int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, day);
		return cal.getTime();
	}

}
