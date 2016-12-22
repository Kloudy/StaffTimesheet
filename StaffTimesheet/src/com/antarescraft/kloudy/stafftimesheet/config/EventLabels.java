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
	
	@ConfigProperty(key = "shift-start", note = "Label displayed in the staff member's logbook when the staff member clocks in")
	private String shiftStart;
	
	@ConfigProperty(key = "shift-end-afk", note = "Label displayed in the staff member's logboo when the staff member is clocked out due to being afk")
	private String shiftEndAfk;
	
	@ConfigProperty(key = "shift-end-disconnected", note = "Label displayed in the staff member's logbook when the staff member is clocked out due to being disconnected")
	private String shiftEndDisconnected;
	
	@ConfigProperty(key = "shift-end-clockout", note = "Label displayed in the staff member's logbook when they clock out")
	private String shiftEndClockout;
	
	@ConfigProperty(key = "shift-end-plugin-disabled", note = "Label displayed in the staff member's logbook when they are clocked out due to the plugin being disabled")
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