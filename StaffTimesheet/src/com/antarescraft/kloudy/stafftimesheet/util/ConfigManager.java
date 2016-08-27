package com.antarescraft.kloudy.stafftimesheet.util;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;

import com.antarescraft.kloudy.hologui.plugincore.messaging.MessageManager;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;

public class ConfigManager
{
	private static ConfigManager instance;
	
	private StaffTimesheet staffTimesheetPlugin;
	
	private String endShiftAFKMessage;
	private String endShiftClockOutMessage;
	private String shiftStartMessage;
	private String shiftEndLabelAFK;
	private String shiftEndLabelDisconnected;
	private String shiftEndLabelClockedOut;
	private String errorMessageNegativeTime;
	private ArrayList<StaffMember> staffMembers;
	
	public static ConfigManager getInstance(StaffTimesheet staffTimesheetPlugin)
	{
		if(instance == null)
		{
			instance = new ConfigManager(staffTimesheetPlugin);
		}
		
		return instance;
	}
	
	private ConfigManager(StaffTimesheet staffTimesheet)
	{
		this.staffTimesheetPlugin = staffTimesheet;
		staffMembers = new ArrayList<StaffMember>();
		
		loadConfigValues();
	}
	
	public void reloadConfig()
	{
		loadConfigValues();
	}
	
	private void loadConfigValues()
	{
		Configuration root = staffTimesheetPlugin.getConfig().getRoot();
		
		endShiftAFKMessage = root.getString("end-shift-afk-message");
		endShiftClockOutMessage = root.getString("end-shift-clock-out-message");
		shiftStartMessage = root.getString("shift-start-message");
		shiftEndLabelAFK = root.getString("shift-end-label-afk");
		shiftEndLabelDisconnected = root.getString("shift-end-label-disconnected");
		shiftEndLabelClockedOut = root.getString("shift-end-label-clocked-out");
		
		ConfigurationSection staffMembersSection = root.getConfigurationSection("staff-members");
		if(staffMembersSection != null)
		{
			for(String key : staffMembersSection.getKeys(false))
			{
				
			}
		}
	}
	
	/*
	 * Getter Functions
	 */
	
	public ArrayList<StaffMember> getStaffMembers()
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
}