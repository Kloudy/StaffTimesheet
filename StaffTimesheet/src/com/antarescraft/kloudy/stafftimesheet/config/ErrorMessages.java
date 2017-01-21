package com.antarescraft.kloudy.stafftimesheet.config;

import com.antarescraft.kloudy.hologuiapi.plugincore.config.ConfigObject;
import com.antarescraft.kloudy.hologuiapi.plugincore.config.ConfigProperty;
import com.antarescraft.kloudy.hologuiapi.plugincore.messaging.MessageManager;

/**
 * Contains all error message strings loaded from config
 * 
 * This class is instantiated populated by the ConfigParser library
 */
public class ErrorMessages implements ConfigObject
{
	private ErrorMessages(){}
	
	@ConfigProperty(key = "duration-underflow")
	private String durationUnderflow;
	
	@ConfigProperty(key = "duration-overflow")
	private String durationOverflow;
	
	@ConfigProperty(key = "not-staff")
	private String notStaff;
	
	@ConfigProperty(key = "not-clocked-in")
	private String notClockedIn;

	@ConfigProperty(key = "already-clocked-in")
	private String alreadyClockedIn;
	
	@ConfigProperty(key = "staff-member-does-not-exist")
	private String staffMemberDoesNotExist;
	
	@ConfigProperty(key = "no-staff-log")
	private String errorMessageNoStaffLog;
	
	@ConfigProperty(key = "invalid-duration-format")
	private String errorMessageInvalidDurationFormat;
	
	@ConfigProperty(key = "invalid-date-format")
	private String errorMessageInvalidDateFormat;
	
	@ConfigProperty(key = "start-date-end-date-mismatch")
	private String errorMessageStartDateEndDateMismatch;

	/*
	 * Getter Functions
	 */

	public String getDurationUnderflow() 
	{
		return MessageManager.setFormattingCodes(durationUnderflow);
	}

	public String getDurationOverflow()
	{
		return MessageManager.setFormattingCodes(durationOverflow);
	}

	public String getNotStaff()
	{
		return MessageManager.setFormattingCodes(notStaff);
	}

	public String getNotClockedIn() 
	{
		return MessageManager.setFormattingCodes(notClockedIn);
	}

	public String getAlreadyClockedIn()
	{
		return MessageManager.setFormattingCodes(alreadyClockedIn);
	}

	public String getStaffMemberDoesNotExist()
	{
		return MessageManager.setFormattingCodes(staffMemberDoesNotExist);
	}

	public String getErrorMessageNoStaffLog()
	{
		return MessageManager.setFormattingCodes(errorMessageNoStaffLog);
	}

	public String getInvalidDurationFormat()
	{
		return MessageManager.setFormattingCodes(errorMessageInvalidDurationFormat);
	}

	public String getErrorMessageInvalidDateFormat()
	{
		return MessageManager.setFormattingCodes(errorMessageInvalidDateFormat);
	}

	public String getErrorMessageStartDateEndDateMismatch()
	{
		return MessageManager.setFormattingCodes(errorMessageStartDateEndDateMismatch);
	}

	@Override
	public void configParseComplete(){}
}