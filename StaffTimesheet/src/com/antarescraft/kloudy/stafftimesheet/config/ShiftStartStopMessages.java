package com.antarescraft.kloudy.stafftimesheet.config;

import com.antarescraft.kloudy.hologuiapi.plugincore.config.*;

import me.clip.placeholderapi.PlaceholderAPI;

/**
 * Contains all shift start/stop strings loaded from config
 * 
 * This class is instantiated populated by the ConfigParser library
 */
public class ShiftStartStopMessages implements ConfigObject
{
	public ShiftStartStopMessages(){}
	
	@ConfigElementKey
	private String shiftStartStopMessages;
	
	@ConfigProperty(key = "shift-end-afk")
	private String shiftEndAfk;
	
	@ConfigProperty(key = "shift-end-clockout")
	private String shiftEndClockout;
	
	@ConfigProperty(key = "shift-start")
	private String shiftStart;
	
	@ConfigProperty(key = "shift-start-return-from-afk")
	private String shiftStartReturnFromAfk;
	
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
	
	public String getShiftStartReturnFromAfk(StaffMember staffMember)
	{
		return PlaceholderAPI.setPlaceholders(staffMember.getPlayer(), shiftStartReturnFromAfk);
	}

	@Override
	public void configParseComplete(){}
}