package com.antarescraft.kloudy.stafftimesheet.config;

import com.antarescraft.kloudy.plugincore.config.*;

import me.clip.placeholderapi.PlaceholderAPI;

/**
 * Contains all shift start/stop strings loaded from config
 * 
 * This class is instantiated populated by the ConfigParser library
 */
public class ShiftStartStopMessages
{
	public ShiftStartStopMessages(){}
	
	@ConfigElementKey(note = "Messages displayed to staff members when they start or stop their shift")
	private String shiftStartStopMessages;
	
	@ConfigProperty(key = "shift-end-afk", note = "Displayed when a staff member is clocked out for being Afk")
	private String shiftEndAfk;
	
	@ConfigProperty(key = "shift-end-clockout", note = "Displayed when a staff member clocks out")
	private String shiftEndClockout;
	
	@ConfigProperty(key = "shift-start", note = "Displayed when a staff member clocks in")
	private String shiftStart;
	
	/*
	 * Getter Functions
	 */
	
	public String getShiftEndAfk(StaffMember staffMember)
	{
		return PlaceholderAPI.setPlaceholders(staffMember.getPlayer(), shiftEndAfk);
	}
	
	public String getShiftEndClockout(StaffMember staffMember)
	{
		return PlaceholderAPI.setPlaceholders(staffMember.getPlayer(), shiftEndClockout);
	}
	
	public String getShiftStart(StaffMember staffMember)
	{
		return PlaceholderAPI.setPlaceholders(staffMember.getPlayer(), shiftStart);
	}
}