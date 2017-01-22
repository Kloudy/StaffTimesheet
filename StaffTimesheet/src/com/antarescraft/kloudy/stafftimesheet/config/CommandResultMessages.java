package com.antarescraft.kloudy.stafftimesheet.config;

import java.util.HashMap;

import com.antarescraft.kloudy.hologuiapi.plugincore.config.*;

/**
 * Contains all command result strings loaded from config
 * 
 * This class is instantiated populated by the ConfigParser library
 */
public class CommandResultMessages implements ConfigObject
{
	private CommandResultMessages(){}
	
	@ConfigElementKey
	private String commandResultMessages;
	
	@ConfigProperty(key = "reset-staff-member-logged-time")
	private String resetStaffMemberLoggedTime;
	
	@ConfigProperty(key = "add-logged-time-for-staff-member")
	private String addLoggedTimeForStaffMember;
	
	@ConfigProperty(key = "subtract-logged-time-for-staff-member")
	private String subtractLoggedTimeForStaffMember;
	
	/*
	 * Getter Functions
	 */
	
	public String getResetStaffMemberLoggedTime()
	{
		return resetStaffMemberLoggedTime;
	}
	
	public String getAddLoggedTimeForStaffMember()
	{
		return addLoggedTimeForStaffMember;
	}
	
	public String getSubtractLoggedTimeForStaffMember()
	{
		return subtractLoggedTimeForStaffMember;
	}

	@Override
	public void configParseComplete(HashMap<String, Object> passthrougParams){}
}