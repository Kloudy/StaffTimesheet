package com.antarescraft.kloudy.stafftimesheet.util;

import com.antarescraft.kloudy.hologuiapi.plugincore.config.annotations.StringConfigurationProperty;

/**
 * Contains all error message strings loaded from config
 * 
 * This class is instantiated populated by the ConfigParser library
 */
public class ConfigErrorMessages
{
	@StringConfigurationProperty(key = "error-message-duration-underflow")
	public final String errorMessageDurationUnderflow = null;
	
	@StringConfigurationProperty(key = "error-message-duration-overflow")
	public final String errorMessageDurationOverflow = null;
	
	@StringConfigurationProperty(key = "error-message-no-staff")
	public final String errorMessageNotStaff = null;
	
	@StringConfigurationProperty(key = "error-message-no-clocked-in")
	public final String errorMessageNotClockedIn = null;
	
	@StringConfigurationProperty(key = "error-message-already-clocked-in")
	public final String errorMessageAlreadyClockedIn = null;
	
	@StringConfigurationProperty(key = "error-message-staff-memeber-does-not-exist")
	public final String errorMessageStaffMemberDoesNotExist = null;
	
	@StringConfigurationProperty(key = "error-message-no-staff-log")
	public final String errorMessageNoStaffLog = null;
	
	@StringConfigurationProperty(key = "error-message-invalid-duration-format")
	public final String errorMessageInvalidDurationFormat = null;
	
	@StringConfigurationProperty(key = "error-message-invalid-date-format")
	public final String errorMessageInvalidDateFormat = null;
	
	@StringConfigurationProperty(key = "error-message-start-date-end-date-mismatch")
	public final String errorMessageStartDateEndDateMismatch = null;
}
