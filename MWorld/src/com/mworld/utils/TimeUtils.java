package com.mworld.utils;

import android.annotation.SuppressLint;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {

	private static Calendar msgCalendar = null;
	private static java.text.SimpleDateFormat dayFormat = null;
	private static java.text.SimpleDateFormat dateFormat = null;
	private static java.text.SimpleDateFormat yearFormat = null;

	@SuppressLint("SimpleDateFormat")
	public static String getTime(String timeStr) {

		long now = System.currentTimeMillis();
		long time = parse(timeStr);

		Calendar nowCalendar = Calendar.getInstance();

		if (msgCalendar == null)
			msgCalendar = Calendar.getInstance();

		msgCalendar.setTimeInMillis(time);

		long calcMills = now - time;

		long calSeconds = calcMills / 1000;

		if (calSeconds < 60) {
			return "刚刚";
		}

		long calMins = calSeconds / 60;

		if (calMins < 60) {

			return new StringBuilder().append(calMins).append("分钟前").toString();
		}

		long calHours = calMins / 60;

		if (calHours < 24 && isSameDay(nowCalendar, msgCalendar)) {
			if (dayFormat == null)
				dayFormat = new java.text.SimpleDateFormat("HH:mm");

			String result = dayFormat.format(msgCalendar.getTime());
			return new StringBuilder().append("今天").append(" ").append(result)
					.toString();

		}

		long calDay = calHours / 24;

		if (calDay < 31) {
			if (isYesterDay(nowCalendar, msgCalendar)) {
				if (dayFormat == null)
					dayFormat = new java.text.SimpleDateFormat("HH:mm");

				String result = dayFormat.format(msgCalendar.getTime());
				return new StringBuilder("昨天").append(" ").append(result)
						.toString();

			} else if (isTheDayBeforeYesterDay(nowCalendar, msgCalendar)) {
				if (dayFormat == null)
					dayFormat = new java.text.SimpleDateFormat("HH:mm");

				String result = dayFormat.format(msgCalendar.getTime());
				return new StringBuilder("前天").append(" ").append(result)
						.toString();

			} else {
				if (dateFormat == null)
					dateFormat = new java.text.SimpleDateFormat("M月d日 HH:mm");

				String result = dateFormat.format(msgCalendar.getTime());
				return new StringBuilder(result).toString();
			}
		}

		long calMonth = calDay / 31;

		if (calMonth < 12 && isSameYear(nowCalendar, msgCalendar)) {
			if (dateFormat == null)
				dateFormat = new java.text.SimpleDateFormat("M月d日 HH:mm");

			String result = dateFormat.format(msgCalendar.getTime());
			return new StringBuilder().append(result).toString();

		}
		if (yearFormat == null)
			yearFormat = new java.text.SimpleDateFormat("yyyy年 M月d日 HH:mm");
		String result = yearFormat.format(msgCalendar.getTime());
		return new StringBuilder().append(result).toString();

	}

	private static boolean isSameDay(Calendar now, Calendar msg) {
		int nowDay = now.get(Calendar.DAY_OF_YEAR);
		int msgDay = msg.get(Calendar.DAY_OF_YEAR);

		return nowDay == msgDay;
	}

	private static boolean isYesterDay(Calendar now, Calendar msg) {
		int nowDay = now.get(Calendar.DAY_OF_YEAR);
		int msgDay = msg.get(Calendar.DAY_OF_YEAR);

		return (nowDay - msgDay) == 1;
	}

	private static boolean isTheDayBeforeYesterDay(Calendar now, Calendar msg) {
		int nowDay = now.get(Calendar.DAY_OF_YEAR);
		int msgDay = msg.get(Calendar.DAY_OF_YEAR);

		return (nowDay - msgDay) == 2;
	}

	private static boolean isSameYear(Calendar now, Calendar msg) {
		int nowYear = now.get(Calendar.YEAR);
		int msgYear = msg.get(Calendar.YEAR);

		return nowYear == msgYear;
	}

	public static final String MONTH = "   " + "Jan" + "Feb" + "Mar" + "Apr"
			+ "May" + "Jun" + "Jul" + "Aug" + "Sep" + "Oct" + "Nov" + "Dec";

	@SuppressWarnings("deprecation")
	public static long parse(String timeStr) {
		Date date = new Date(timeStr);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();

		// try {
		// String[] tokens = timeStr.split(" ");
		// StringBuilder sBuilder = new StringBuilder();
		// int m = MONTH.indexOf(tokens[1]) / 3;
		// String month = m < 10 ? "0" + m : "" + m;
		// sBuilder.append(tokens[5]).append(".").append(month).append(".")
		// .append(tokens[2]).append(" ").append(tokens[3]);
		// return sBuilder.toString();
		// } catch (NumberFormatException e) {
		// e.printStackTrace();
		// }
	}

}
