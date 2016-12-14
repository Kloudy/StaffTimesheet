package com.antarescraft.kloudy.stafftimesheet.config;

import com.antarescraft.kloudy.hologuiapi.plugincore.config.ConfigElementKey;
import com.antarescraft.kloudy.hologuiapi.plugincore.config.ConfigProperty;

/**
 * Contains all error message strings loaded from config
 * 
 * This class is instantiated populated by the ConfigParser library
 */
public class ErrorMessages
{
	public ErrorMessages(){}
	
	@ConfigElementKey(note = "List of error messages")
	private String errorMessages;
	
	@ConfigProperty(key = "duration-underflow", note = "Error message displayed if a super admin tries to remove more time from a staff member than they have currently logged.")
	private String durationUnderflow;
	
	@ConfigProperty(key = "duration-overflow", note = "Error message displayed if a super admin tries to add more time to a staff member's time than the max duration value")
	private String durationOverflow;
	
	@ConfigProperty(key = "no-staff", note = "Error message displayed to the player if they attempt /staff shift start but they are not configured in staff-members.yml")
	private String notStaff;
	
	@ConfigProperty(key = "not-clocked-in", note = "Error message displayed if a player tries to use /staff shift end but they are not currently on the clock")
	private String notClockedIn;

	@ConfigProperty(key = "already-clocked-in", note = "Error message displayed if the player tries to clock in if they are already clocked in")
	private String alreadyClockedIn;
	
	@ConfigProperty(key = "staff-member-does-not-exist", note = "Error message displayed when an a staff command is run with a player name that doesn't belong to any staff member")
	private String staffMemberDoesNotExist;
	
	@ConfigProperty(key = "no-staff-log", note = "Error message displayed when the plugin is unable to load a staff member's logs")
	private String errorMessageNoStaffLog;
	
	@ConfigProperty(key = "invalid-duration-format", note = "Error message displayed when an invalid duration format is entered from the user ")
	private String errorMessageInvalidDurationFormat;
	
	@ConfigProperty(key = "invalid-date-format", note = "Error message displayed when an invalid date format is entered from the user")
	private String errorMessageInvalidDateFormat;
	
	@ConfigProperty(key = "start-date-end-date-mismatch", note = "Error message displayed when the user enters a range of dates in a command but the start date comes after the end date")
	private String errorMessageStartDateEndDateMismatch;

	/*
	 * Getter Functions
	 */

	public String getDurationUnderflow() 
	{
		return durationUnderflow;
	}

	public String getDurationOverflow()
	{
		return durationOverflow;
	}

	public String getNotStaff()
	{
		return notStaff;
	}

	public String getNotClockedIn() 
	{
		return notClockedIn;
	}

	public String getAlreadyClockedIn()
	{
		return alreadyClockedIn;
	}

	public String getStaffMemberDoesNotExist()
	{
		return staffMemberDoesNotExist;
	}

	public String getErrorMessageNoStaffLog()
	{
		return errorMessageNoStaffLog;
	}

	public String getInvalidDurationFormat()
	{
		return errorMessageInvalidDurationFormat;
	}

	public String getErrorMessageInvalidDateFormat()
	{
		return errorMessageInvalidDateFormat;
	}

	public String getErrorMessageStartDateEndDateMismatch()
	{
		return errorMessageStartDateEndDateMismatch;
	}
}