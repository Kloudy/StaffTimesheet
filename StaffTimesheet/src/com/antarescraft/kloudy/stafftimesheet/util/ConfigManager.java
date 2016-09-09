package com.antarescraft.kloudy.stafftimesheet.util;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.plugincore.messaging.MessageManager;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;

public class ConfigManager
{	
	private static StaffTimesheet staffTimesheetPlugin;
	
	private String shiftEndAFKMessage;
	private String shiftEndClockoutMessage;
	private String shiftStartMessage;
	private String shiftStartLabel;
	private String shiftEndLabelAFK;
	private String shiftEndLabelDisconnected;
	private String shiftEndLabelClockedOut;
	private String errorMessageNegativeTime;
	private String errorMessageNotStaff;
	private String errorMessageNotClockedIn;
	private HashMap<UUID, StaffMember> staffMembers;
	
	public ConfigManager(StaffTimesheet staffTimesheet)
	{
		staffTimesheetPlugin = staffTimesheet;
		staffMembers = new HashMap<UUID, StaffMember>();
		
		loadConfigValues();
	}
	
	public void loadConfigValues()
	{
		staffMembers.clear();
		
		staffTimesheetPlugin.reloadConfig();
		Configuration root = staffTimesheetPlugin.getConfig().getRoot();
		
		StaffTimesheet.debugMode = root.getBoolean("debug-mode", false);
		
		shiftEndAFKMessage = root.getString("shift-end-afk-message", "").replace("&", "§");
		shiftEndClockoutMessage = root.getString("shift-end-clockout-message", "").replace("&", "§");
		shiftStartMessage = root.getString("shift-start-message", "").replace("&", "§");
		shiftStartLabel = root.getString("shift-start-label").replace("&", "§");;
		shiftEndLabelAFK = root.getString("shift-end-label-afk").replace("&", "§");
		shiftEndLabelDisconnected = root.getString("shift-end-label-disconnected", "").replace("&", "§");
		shiftEndLabelClockedOut = root.getString("shift-end-label-clocked-out", "").replace("&", "§");
		
		YamlConfiguration staffMembersYaml = YamlConfiguration.loadConfiguration(new File(String.format("plugins/%s/staff-members.yml", staffTimesheetPlugin.getName())));
		ConfigurationSection staffMembersSection = staffMembersYaml.getConfigurationSection("staff-members");
		
		if(staffMembersSection != null)
		{
			for(String key : staffMembersSection.getKeys(false))
			{
				String playerName = key;
				String playerUUID = staffMembersSection.getString(playerName + ".uuid");
				boolean superAdmin = staffMembersSection.getBoolean(playerName + ".super-admin");
				String clockInPermission = staffMembersSection.getString(playerName + ".clock-in-permission");
				String timeGoal = staffMembersSection.getString(playerName + ".time-goal", "15:00:00");
				String rankTitle = staffMembersSection.getString(playerName + ".rank-title");
				String loggedTime = staffMembersSection.getString(playerName + ".logged-time", "00:00:00");
				boolean startShiftOnLogin = staffMembersSection.getBoolean("start-shift-on-login", false);
				
				StaffMember staffMember = new StaffMember(playerName, playerUUID, superAdmin, clockInPermission,
						timeGoal, rankTitle, loggedTime, startShiftOnLogin);
				
				staffMembers.put(UUID.fromString(playerUUID), staffMember);
			}
		}
		
		errorMessageNegativeTime = root.getString("error-message-negative-time", "").replace("&", "§");
		errorMessageNotStaff = root.getString("error-message-not-staff", "").replace("&", "§");
		errorMessageNotClockedIn = root.getString("error-message-not-clocked-in", "").replace("&", "§");
	}
	
	public static void writePropertyToConfigFile(String path, Object value)
	{
		try
		{	
			File configFile = new File("plugins/" + staffTimesheetPlugin.getName() + "/staff-members.yml");
			YamlConfiguration yaml = YamlConfiguration.loadConfiguration(configFile);
			yaml.set(path, value);
			yaml.save(configFile);
			
		}catch(Exception e){MessageManager.error(Bukkit.getConsoleSender(), 
				String.format("[%s]Error saving values to the config file. Does the file still exist?", staffTimesheetPlugin.getName()));}
	}
	
	private String setPlaceholders(StaffMember staffMember, String text)
	{	
		text = text.replace("%stafftimesheet_current-logged-time%", "(hh:mm:ss) " + staffMember.getLoggedTime());
		text = text.replace("%stafftimesheet_time-goal%", "(hh:mm:ss) " + staffMember.getTimeGoal());

		return text;
	}
	
	/*
	 * Getter Functions
	 */
	
	public StaffMember getStaffMember(Player player)
	{
		return staffMembers.get(player.getUniqueId());
	}
	
	public StaffMember getStaffMember(String playerName)
	{
		for(StaffMember staffMember : staffMembers.values())
		{
			if(staffMember.getPlayerName().equals(playerName))
			{
				return staffMember;
			}
		}
		
		return null;
	}
	
	public String getEndShiftAFKMessage(StaffMember staffMember)
	{
		return setPlaceholders(staffMember, shiftEndAFKMessage);
	}
	
	public String getEndShiftClockOutMessage(StaffMember staffMember)
	{
		return setPlaceholders(staffMember, shiftEndClockoutMessage);
	}
	
	public String getShiftStartMessage(StaffMember staffMember)
	{
		return setPlaceholders(staffMember, shiftStartMessage);
	}
	
	public String getShiftStartLabel()
	{
		return shiftStartLabel;
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
	
	public String getErrorMessageNotClockedIn()
	{
		return errorMessageNotClockedIn;
	}
}