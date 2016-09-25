package com.antarescraft.kloudy.stafftimesheet.util;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

import com.antarescraft.kloudy.stafftimesheet.exceptions.InvalidDateFormatException;
import com.antarescraft.kloudy.stafftimesheet.exceptions.InvalidDurationFormatException;

/**
 * Utility class for doing operations on date time formats
 */
public class TimeFormat
{
	public static Duration parseTimeFormat(String timeFormat) throws InvalidDurationFormatException
	{
		if(!timeFormat.matches("\\d{2}:\\d{2}:\\d{2}"))//check format is ##:##:##
		{
			throw new InvalidDurationFormatException();
		}
		
		Duration time = Duration.ZERO;
		
		String[] timeTokens = timeFormat.split(":");
		if(timeTokens.length == 3)
		{
			try
			{
				time = time.plusHours(Long.parseLong(timeTokens[0]));
				time = time.plusMinutes(Long.parseLong(timeTokens[1]));
				time = time.plusSeconds(Long.parseLong(timeTokens[2]));
			}
			catch(NumberFormatException e)
			{
				throw new InvalidDurationFormatException();
			}
		}

		return time;
	}
	
	public static String getTimeFormat(Duration time)
	{
		long hours = time.toHours();
		long minutes = time.toMinutes() % 60;
		long seconds = time.getSeconds() % 60;
				
		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
	
	public static String getDateFormat(Calendar date)
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
		return simpleDateFormat.format(date.getTime());
	}
	
	public static Calendar parseDateFormat(String dateFormat) throws InvalidDateFormatException
	{
		if(!dateFormat.matches("\\d{4}/\\d{2}/\\d{2}"))//check format is ####/##/##
		{
			throw new InvalidDateFormatException();
		}
		
		String[] dateTokens = dateFormat.split("/");
		
		try
		{
			int year = Integer.parseInt(dateTokens[0]);
			int month = Integer.parseInt(dateTokens[1]);
			int date = Integer.parseInt(dateTokens[2]);
			
			if(year <= 0 || month <= 0 || month > 12 || date <= 0 || date > 31)//check for bad input
			{
				throw new InvalidDateFormatException();
			}
						
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, month-1, date);
			
			return calendar;
		}
		catch(NumberFormatException e)
		{
			throw new InvalidDateFormatException();
		}
	}
	
	public static String generateTimestamp(String dateFormat)
	{
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		
		return simpleDateFormat.format(now);
	}
}