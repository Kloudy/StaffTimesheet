package com.antarescraft.kloudy.stafftimesheet;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Represents a single log on a particular day
 */
public class Log 
{
	private Calendar date;
	private ArrayList<String> lines;
	
	public Log(Calendar date, ArrayList<String> lines)
	{
		this.date = date;
		this.lines = lines;
	}
	
	public Calendar getDate()
	{
		return date;
	}
	
	public ArrayList<String> getLines()
	{
		return lines;
	}
}