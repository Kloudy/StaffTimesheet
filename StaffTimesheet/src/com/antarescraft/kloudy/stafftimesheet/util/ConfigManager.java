package com.antarescraft.kloudy.stafftimesheet.util;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;

public class ConfigManager
{	
	private StaffTimesheet staffTimesheetPlugin;
	
	private String endShiftAFKMessage;
	private String endShiftClockOutMessage;
	private String shiftStartMessage;
	private String shiftEndLabelAFK;
	private String shiftEndLabelDisconnected;
	private String shiftEndLabelClockedOut;
	private String errorMessageNegativeTime;
	private String errorMessageNotStaff;
	private HashMap<UUID, StaffMember> staffMembers;
	
	public ConfigManager(StaffTimesheet staffTimesheet)
	{
		this.staffTimesheetPlugin = staffTimesheet;
		staffMembers = new HashMap<UUID, StaffMember>();
		
		loadConfigValues();
	}
	
	public void loadConfigValues()
	{
		staffMembers.clear();
		
		Configuration root = staffTimesheetPlugin.getConfig().getRoot();
		
		endShiftAFKMessage = root.getString("end-shift-afk-message", "").replace("&", "§");
		endShiftClockOutMessage = root.getString("end-shift-clock-out-message", "").replace("&", "§");
		shiftStartMessage = root.getString("shift-start-message", "").replace("&", "§");
		shiftEndLabelAFK = root.getString("shift-end-label-afk").replace("&", "§");
		shiftEndLabelDisconnected = root.getString("shift-end-label-disconnected", "").replace("&", "§");
		shiftEndLabelClockedOut = root.getString("shift-end-label-clocked-out", "").replace("&", "§");
		
		ConfigurationSection staffMembersSection = root.getConfigurationSection("staff-members");
		if(staffMembersSection != null)
		{
			for(String key : staffMembersSection.getKeys(false))
			{
				String playerName = key;
				String playerUUID = staffMembersSection.getString("uuid");
				boolean superAdmin = staffMembersSection.getBoolean("super-admin");
				String clockInPermission = staffMembersSection.getString("clock-in-permission");
				String monthlyTimeGoal = staffMembersSection.getString("monthly-time-goal");
				String rankTitle = staffMembersSection.getString("rank-title");
				String hoursLoggedThisMonth = staffMembersSection.getString("hours-logged-this-month");
				
				StaffMember staffMember = new StaffMember(playerName, playerUUID, superAdmin, clockInPermission,
						monthlyTimeGoal, rankTitle, hoursLoggedThisMonth);
				
				staffMembers.put(UUID.fromString(playerUUID), staffMember);
			}
		}
		
		errorMessageNegativeTime = root.getString("error-message-negative-time", "").replace("&", "§");
		errorMessageNotStaff = root.getString("error-message-not-staff", "").replace("&", "§");
	}
	
	/*
	 * Getter Functions
	 */
	
	public HashMap<UUID, StaffMember> getStaffMembers()
	{
		return staffMembers;
	}
	
	public String getEndShiftAFKMessage()
	{
		return endShiftAFKMessage;
	}
	
	public String getEndShiftClockOutMessage()
	{
		return endShiftClockOutMessage;
	}
	
	public String getShiftStartMessage()
	{
		return shiftStartMessage;
	}
	
	public String getShiftEndLabelAFK()
	{
		return shiftEndLabelAFK;
	}
	
	public String getShiftEndLabelDisconnected()
	{
		return shiftEndLabelDisconnected;
	}
	
	public String getShiftLabelClockedOut()
	{
		return shiftEndLabelClockedOut;
	}
	
	public String getErrorMessageNegativeTime()
	{
		return errorMessageNegativeTime;
	}
	
	public String getErrorMessageNotStaff()
	{
		return errorMessageNotStaff;
	}
}