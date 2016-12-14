package com.antarescraft.kloudy.stafftimesheet.config;

import com.antarescraft.kloudy.hologuiapi.plugincore.config.ConfigElementKey;
import com.antarescraft.kloudy.hologuiapi.plugincore.config.ConfigProperty;

/**
 * Contains all event label strings loaded from config
 * 
 * This class is instantiated populated by the ConfigParser library
 */
public class EventLabels 
{
	public EventLabels() {}
	
	@ConfigElementKey(note = "List of event labels. These labels are displayed in a staff member's logbook")
	private String eventLabels;
	
	@ConfigProperty(key = "shift-start-label", note = "Label displayed in the staff member's logbook when the staff member clocks in")
	private String shiftStart;
	
	@ConfigProperty(key = "shift-end-label-afk", note = "Label displayed in the staff member's logboo when the staff member is clocked out due to being afk")
	private String shiftEndAfk;
	
	@ConfigProperty(key = "shift-end-label-disconnected", note = "Label displayed in the staff member's logbook when the staff member is clocked out due to being disconnected")
	private String shiftEndDisconnected;
	
	@ConfigProperty(key = "shift-end-label-clocked-out", note = "Label displayed in the staff member's logbook when they clock out")
	private String shiftEndClockedOut;
	
	@ConfigProperty(key = "shift-end-label-plugin-disabled", note = "Label displayed in the staff member's logbook when they are clocked out due to the plugin being disabled")
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
	
	public String getShiftEndClockedOut()
	{
		return shiftEndClockedOut;
	}
	
	public String getShiftEndPluginDisabled()
	{
		return shiftEndPluginDisabled;
	}
}