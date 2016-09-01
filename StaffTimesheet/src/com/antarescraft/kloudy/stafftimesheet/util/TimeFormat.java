package com.antarescraft.kloudy.stafftimesheet.util;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;

/**
 * Utility class for doing operations on date time formats
 */
public class TimeFormat
{
	public static Duration parseTimeFormat(String timeFormat)
	{
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
			catch(NumberFormatException e){}
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
	
	public static String generateTimestamp(String dateFormat)
	{
		Calendar calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		
		return simpleDateFormat.format(now);
	}
}