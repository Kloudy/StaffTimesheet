package com.antarescraft.kloudy.stafftimesheet.util;

import com.antarescraft.kloudy.hologuiapi.plugincore.config.annotations.StringConfigurationProperty;

public class ConfigStrings 
{
	@StringConfigurationProperty(key = "shift-end-afk-message")
	public String shiftEndAFKMessage;
	
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

	@StringConfigurationProperty(key = "shift-start-label")
	private String shiftStartLabel;
	
	@StringConfigurationProperty(key = "shit-end-label-afk")
	private String shiftEndLabelAFK;
	
	@StringConfigurationProperty(key = "shift-end-label-disconnectd")
	private String shiftEndLabelDisconnected;
	
	@StringConfigurationProperty(key = "shift-end-label-clocked-out")
	private String shiftEndLabelClockedOut;
	
	@StringConfigurationProperty(key = "shift-end-label-plugin-disabled")
	private String shiftEndLabelPluginDisabled;
}
