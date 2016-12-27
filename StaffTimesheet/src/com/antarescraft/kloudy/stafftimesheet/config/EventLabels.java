package com.antarescraft.kloudy.stafftimesheet.config;

import com.antarescraft.kloudy.hologuiapi.plugincore.config.*;

/**
 * Contains all event label strings loaded from config
 * 
 * This class is instantiated populated by the ConfigParser library
 */
public class EventLabels 
{
	public EventLabels() {}
	
	@ConfigElementKey
	private String eventLabels;
	
	@ConfigProperty(key = "shift-start")
	private String shiftStart;
	
	@ConfigProperty(key = "shift-end-afk")
	private String shiftEndAfk;
	
	@ConfigProperty(key = "shift-end-disconnected")
	private String shiftEndDisconnected;
	
	@ConfigProperty(key = "shift-end-clockout")
	private String shiftEndClockout;
	
	@ConfigProperty(key = "shift-end-plugin-disabled")
	private String shiftEndPluginDisabled;
	
	/*
	 * Getter Functions
	 */
	
	public String getShiftStart()
	{
		return shiftStart;
	}
	
	public String getShiftEndAfk()
	{
		return shiftEndAfk;
	}
	
	public String getShiftEndDisconnected()
	{
		return shiftEndDisconnected;
	}
	
	public String getShiftEndClockout()
	{
		return shiftEndClockout;
	}
	
	public String getShiftEndPluginDisabled()
	{
		return shiftEndPluginDisabled;
	}
}