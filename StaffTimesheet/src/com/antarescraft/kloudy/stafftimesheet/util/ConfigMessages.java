package com.antarescraft.kloudy.stafftimesheet.util;

import com.antarescraft.kloudy.hologuiapi.plugincore.config.annotations.StringConfigurationProperty;

public class ConfigMessages 
{
	@StringConfigurationProperty(key = "shift-end-afk-message")
	private String shiftEndAFKMessage;
	
	@StringConfigurationProperty(key = "shift-end-clockout-message")
	private String shiftEndClockoutMessage;
	
	@StringConfigurationProperty(key = "shift-start-message")
	private String shiftStartMessage;
	
	@StringConfigurationProperty(key = "reset-staff-member-logged-time-message")
	private String resetStaffMemberLoggedTimeMessage;
	
	@StringConfigurationProperty(key = "add-logged-time-for-staff-member-message")
	private String addLoggedTimeForStaffMemberMessage;
	
	@StringConfigurationProperty(key = "subtract-logged-time-for-staff-member-message")
	private String subtractLoggedTimeForStaffMemberMessage;
	
	@StringConfigurationProperty(key = "loading-staff-member-logbook-message")
	private String loadingStaffMemberLogbookMessage;
	
	@StringConfigurationProperty(key = "loaded-staff-member-logbook-message")
	private String loadedStaffMemberLogbookMessage;
	
	@StringConfigurationProperty(key = "error-message-duration-underflow")
	private String errorMessageDurationUnderflow;
	
	private String errorMessageDurationOverflow;
	private String errorMessageNotStaff;
	private String errorMessageNotClockedIn;
	private String errorMessageAlreadyClockedIn;
	private String errorMessageStaffMemberDoesNotExist;
	private String errorMessageNoStaffLog;
	private String errorMessageInvalidDurationFormat;
	private String errorMessageInvalidDateFormat;
	private String errorMessageStartDateEndDateMismatch;
	
	private String shiftStartLabel;
	private String shiftEndLabelAFK;
	private String shiftEndLabelDisconnected;
	private String shiftEndLabelClockedOut;
	private String shiftEndLabelPluginDisabled;
}
