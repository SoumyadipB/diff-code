package com.ericsson.isf.util;

import java.sql.Time;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.ericsson.isf.exception.ApplicationException;

public class DateTimeUtil {

	private static final String SINGLE_QUOTE_EXCLAMATION_MARK = "'!";
	private static final String SINGLE_QUOTE = " '";

	public static boolean validateDateTimeByCurrentDateTime(String dateTime, String format) {

		Calendar cal = Calendar.getInstance();
		Date date = convertStringToDate(dateTime, format);
		cal.setTime(date);

		if (cal.getTime().before(Calendar.getInstance().getTime())) {
			throw new ApplicationException(500, ApplicationMessages.INVALID_DATE_BY_CURRENTDATE);
		}
		return true;

	}

	public static boolean validateTimeInHourFormat(String time, String hourRegex) {
		if (StringUtils.isBlank(time)) {
			throw new ApplicationException(500, ApplicationMessages.INVALID_TIME_24HOUR_PATTERN);
		}
		Pattern pattern = Pattern.compile(hourRegex);
		Matcher matcher = pattern.matcher(time);

		if (matcher.matches()) {
			return true;
		}
		throw new ApplicationException(500, ApplicationMessages.INVALID_TIME_24HOUR_PATTERN);
	}

	public static Date convertStringToDate(String dateStr, String format) {
		try {
			if (StringUtils.isBlank(dateStr)) {
				throw new ApplicationException(500, ApplicationMessages.EMPTY_DATE);
			}
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			formatter.setLenient(false);
			return formatter.parse(dateStr);
		} catch (ParseException e) {
			throw new ApplicationException(500,
					ApplicationMessages.INVALID_DATE_FORMAT + SINGLE_QUOTE + format + SINGLE_QUOTE_EXCLAMATION_MARK);
		}
	}
	
	public static String convertDateToString(Date date,String format) throws ParseException 
	{
		Format formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}
	
	public static Date convertDateAndTimeToDate(String stringDate, Time time) {

		String date = stringDate + StringUtils.SPACE + time;
		return PlannedEndDateCal.convertStringToDate(date, AppConstants.UI_DATE_FORMAT);
	}
	
	public static Date changeTimeZone(Date date, String timeZone, String dateFormat) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(dateFormat);
		formatter.setTimeZone(TimeZone.getTimeZone(timeZone)); // Or whatever IST is supposed to be
		String dateString=formatter.format(date);
		date=convertStringToDate(dateString, dateFormat);
		return date;
	}
}
