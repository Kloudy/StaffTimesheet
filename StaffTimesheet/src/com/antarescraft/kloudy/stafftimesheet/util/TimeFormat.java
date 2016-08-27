package com.antarescraft.kloudy.stafftimesheet.util;

/**
 * Represents a TimeFormat serializable from a format string with format: "hh:mm:ss"
 * Max time value: 99:59:59 (99 hours, 59 minutes, 59 seconds)
 * Min time value: 00:00:00 (0 hours, 0 minutes, 0 seconds)
 */
public class TimeFormat 
{
	public static final TimeFormat MAX_VALUE = parseTimeFormat("99:59:59");
	public static final TimeFormat MIN_VALUE = parseTimeFormat("00:00:00");
	
	private int hours;
	private int minutes;
	private int seconds;
	
	private TimeFormat(int hours, int minutes, int seconds)
	{
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
	}
	
	/**
	 * Parses the timeFormat string to a TimeFormat object.
	 * 
	 * @param timeFormat Time format string of format: "hh:mm:ss"
	 * @return TimeFormat object representation of the time format string. Returns null if the timeFormat string has an invalid format.
	 */
	public static TimeFormat parseTimeFormat(String timeFormat)
	{
		String[] tokens = timeFormat.split(":");
		
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		
		if(tokens.length == 3)
		{
			try
			{
				seconds = Integer.parseInt(tokens[2]);
				if(seconds < 0)
				{
					seconds = 0;
				}
				if(seconds > 59)
				{
					minutes += seconds / 60;
					seconds  = seconds % 60;
				}
			}catch(NumberFormatException e){}
			
			try
			{
				minutes = Integer.parseInt(tokens[1]);
				if(minutes < 0)
				{
					minutes = 0;
				}
				if(minutes > 59)
				{
					hours += minutes / 60;
					minutes = minutes % 60;
				}
			}catch(NumberFormatException e){}
			
			try
			{
				hours = Integer.parseInt(tokens[0]);
				if(hours < 0)
				{
					hours = 0;
				}
				else if(hours > 99)
				{
					return MAX_VALUE;
				}
			}catch(NumberFormatException e){}

			return new TimeFormat(hours, minutes, seconds);
		}
		
		return null;
	}
	
	/**
	 * Adds the specified timeFormat to this TimeFormat object
	 * 
	 * @param timeFormat
	 * @return TimeFormat representing this instance of TimeFormat added to timeFormat
	 */
	public TimeFormat addTime(TimeFormat timeFormat)
	{
		int hours = this.hours + timeFormat.getHours();
		int minutes = this.minutes + timeFormat.getMinutes();
		int seconds = this.seconds + timeFormat.getSeconds();
		
		if(seconds > 59)
		{
			minutes++;
			seconds -= 60;
		}
		
		if(minutes > 59)
		{
			hours++;
			minutes -= 60;
		}
		
		if(hours > 99)
		{
			return MAX_VALUE;
		}
		
		return new TimeFormat(hours, minutes, seconds);
	}
	
	public TimeFormat subtractTime(TimeFormat timeFormat)
	{
		//TODO
		return null;
	}
	
	/*
	 * Getter Functions
	 */
	public int getHours()
	{
		return hours;
	}
	
	public int getMinutes()
	{
		return minutes;
	}
	
	public int getSeconds()
	{
		return seconds;
	}
	
	@Override
	public String toString()
	{
		return hours + ":" + minutes + ":" + seconds;
	}
}