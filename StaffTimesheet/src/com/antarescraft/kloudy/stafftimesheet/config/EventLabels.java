package com.antarescraft.kloudy.stafftimesheet.config;

import com.antarescraft.kloudy.hologuiapi.plugincore.config.*;
import com.antarescraft.kloudy.hologuiapi.plugincore.config.annotations.*;

/**
 * Contains all event label strings loaded from config
 * 
 * This class is instantiated populated by the ConfigParser library
 */
public class EventLabels implements ConfigObject
{
	public EventLabels() {}
	
	@ConfigProperty(key = "shift-start")
	private String shiftStart;
	
	@ConfigProperty(key = "shift-start-return-from-afk")
	private String shiftStartReturnFromAfk;
	
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
	
	public String getShiftStartReturnFromAfk()
	{
		return shiftStartReturnFromAfk;
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

	@Override
	public void configParseComplete(PassthroughParams params){}
}