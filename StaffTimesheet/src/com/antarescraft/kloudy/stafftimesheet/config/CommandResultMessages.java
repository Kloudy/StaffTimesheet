package com.antarescraft.kloudy.stafftimesheet.config;

import com.antarescraft.kloudy.plugincore.config.*;

/**
 * Contains all command result strings loaded from config
 * 
 * This class is instantiated populated by the ConfigParser library
 */
public class CommandResultMessages
{
	public CommandResultMessages(){}
	
	@ConfigElementKey(note = "List of command result messages")
	private String commandResultMessages;
	
	@ConfigProperty(key = "reset-staff-member-logged-time", note = "Message displayed after command /staff manage <player_name> reset is successfully executed")
	private String resetStaffMemberLoggedTime;
	
	@ConfigProperty(key = "add-logged-time-for-staff-member", note = "Message displayed after command /staff manage <player_name> add <formatted_time> is successfully executed ")
	private String addLoggedTimeForStaffMember;
	
	@ConfigProperty(key = "subtract-logged-time-for-staff-member", note = "Message displayed after command /staff manage <player_name> subtract <formatted_time> is successfully executed                             #Message displayed after command /staff logbook <player_name> <start_date> [end_date] is successfully executed and the command invoker is given the logbook")
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
}