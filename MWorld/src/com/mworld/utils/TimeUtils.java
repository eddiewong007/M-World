package com.mworld.utils;


public class TimeUtils {
	public static final String MONTH = "   " + "Jan" + "Feb" + "Mar" + "Apr"
			+ "May" + "Jun" + "Jul" + "Aug" + "Sep" + "Oct" + "Nov" + "Dec";

	public static String parse(String timeStr) {

		try {
			String[] tokens = timeStr.split(" ");
			StringBuilder sBuilder = new StringBuilder();
			int m = MONTH.indexOf(tokens[1]) / 3;
			String month = m < 10 ? "0" + m : "" + m;
			sBuilder.append(tokens[5]).append(".").append(month).append(".")
					.append(tokens[2]).append(" ").append(tokens[3]);
			return sBuilder.toString();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return "";
	}
	// String[] timeTokens = tokens[3].split(":");
	// int monthDay = Integer.parseInt(tokens[2]);
	// int hour = Integer.parseInt(timeTokens[0]);
	// int minute = Integer.parseInt(timeTokens[1]);
	// int second = Integer.parseInt(timeTokens[2]);
}
