package com.antarescraft.kloudy.stafftimesheet.util;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.plugincore.messaging.MessageManager;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;
import com.antarescraft.kloudy.stafftimesheet.exceptions.InvalidDurationFormatException;

import net.md_5.bungee.api.ChatColor;

public class ConfigManager
{	
	private static StaffTimesheet staffTimesheetPlugin;
	
	private int logCycleDuration;
	private Duration updateStaffLogsPeriod;
	private String shiftEndAFKMessage;
	private String shiftEndClockoutMessage;
	private String shiftStartMessage;
	private String resetStaffMemberLoggedTimeMessage;
	private String addLoggedTimeForStaffMemberMessage;
	private String subtractLoggedTimeForStaffMemberMessage;
	private String loadingStaffMemberLogbookMessage;
	private String loadedStaffMemberLogbookMessage;
	private int maxLogRange;
	private String logbookTextHeader;
	private List<String> logbookLoreText;
	private String shiftStartLabel;
	private String shiftEndLabelAFK;
	private String shiftEndLabelDisconnected;
	private String shiftEndLabelClockedOut;
	private String errorMessageNegativeTime;
	private String errorMessageNotStaff;
	private String errorMessageNotClockedIn;
	private String errorMessageStaffMemberDoesNotExist;
	private String errorMessageNoStaffLog;
	private String errorMessageInvalidDurationFormat;
	private String errorMessageInvalidDateFormat;
	private String errorMessageStartDateEndDateMismatch;
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
		
		logCycleDuration = root.getInt("log-cycle-duration", 4);
		
		try
		{
			updateStaffLogsPeriod = TimeFormat.parseTimeFormat(root.getString("update-staff-logs-period", "00:01:00"));
		}
		catch(InvalidDurationFormatException e)
		{
			this.logInvalidDurationConfigValue("update-staff-logs-period", "00:01:00");
			try 
			{
				updateStaffLogsPeriod = TimeFormat.parseTimeFormat("00:01:00");
			} catch (InvalidDurationFormatException de) {}
		}
		
		shiftEndAFKMessage = root.getString("shift-end-afk-message", "").replace("&", "§");
		shiftEndClockoutMessage = root.getString("shift-end-clockout-message", "").replace("&", "§");
		shiftStartMessage = root.getString("shift-start-message", "").replace("&", "§");
		resetStaffMemberLoggedTimeMessage = root.getString("reset-staff-member-logged-time-message", "").replace("&", "§");
		addLoggedTimeForStaffMemberMessage = root.getString("add-logged-time-for-staff-member-message", "").replace("&", "§");
		subtractLoggedTimeForStaffMemberMessage = root.getString("subtract-logged-time-for-staff-member-message", "").replace("&", "§");
		loadingStaffMemberLogbookMessage = root.getString("loading-staff-member-logbook-message", "").replace("&", "§");
		loadedStaffMemberLogbookMessage = root.getString("loaded-staff-member-logbook-message", "").replace("&", "§");
		maxLogRange = root.getInt("max-log-range", 365);
		logbookTextHeader = root.getString("logbook-text-header", "").replace("&", "§");
		logbookLoreText = root.getStringList("logbook-lore-text");
		shiftStartLabel = root.getString("shift-start-label").replace("&", "§");;
		shiftEndLabelAFK = root.getString("shift-end-label-afk").replace("&", "§");
		shiftEndLabelDisconnected = root.getString("shift-end-label-disconnected", "").replace("&", "§");
		shiftEndLabelClockedOut = root.getString("shift-end-label-clocked-out", "").replace("&", "§");
		
		for(String loreTextLine : logbookLoreText)
		{
			loreTextLine = loreTextLine.replace("&", "§");
		}
		
		File staffMembersYmlFile = new File(String.format("plugins/%s/staff-members.yml", staffTimesheetPlugin.getName()));
		if(!staffMembersYmlFile.exists())
		{
			IOManager.initFileStructure(staffTimesheetPlugin);
		}
		
		//parse staff-members.yml
		YamlConfiguration staffMembersYaml = YamlConfiguration.loadConfiguration(staffMembersYmlFile);
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
				
				Duration timeGoalDuration = null;
				Duration loggedTimeDuration = null;
				
				//check 'time-goal-duration' format
				try
				{
					timeGoalDuration = TimeFormat.parseTimeFormat(timeGoal);
				}
				catch(InvalidDurationFormatException e)
				{
					logInvalidDurationConfigValue(playerName + ".time-goal", "15:00:00");
					
					try 
					{
						timeGoalDuration = TimeFormat.parseTimeFormat("15:00:00");
					} catch (InvalidDurationFormatException e1) {}
				}
				
				//check 'logged-time' format
				try
				{
					loggedTimeDuration = TimeFormat.parseTimeFormat(loggedTime);
				}
				catch(InvalidDurationFormatException e)
				{
					logInvalidDurationConfigValue(playerName + ".logged-time", "00:00:00");
					
					try 
					{
						loggedTimeDuration = TimeFormat.parseTimeFormat("00:00:00");
					} catch (InvalidDurationFormatException e1) {}
				}
				
				
				StaffMember staffMember = new StaffMember(playerName, playerUUID, superAdmin, clockInPermission,
						timeGoalDuration, rankTitle, loggedTimeDuration, startShiftOnLogin);
				
				
				staffMembers.put(UUID.fromString(playerUUID), staffMember);
			}
		}
		
		errorMessageNegativeTime = root.getString("error-message-negative-time", "").replace("&", "§");
		errorMessageNotStaff = root.getString("error-message-not-staff", "").replace("&", "§");
		errorMessageNotClockedIn = root.getString("error-message-not-clocked-in", "").replace("&", "§");
		errorMessageStaffMemberDoesNotExist = root.getString("error-message-staff-member-does-not-exist", "").replace("&", "§");
		errorMessageNoStaffLog = root.getString("error-message-no-staff-log", "").replace("&", "§");
		errorMessageInvalidDurationFormat = root.getString("error-message-invalid-duration-format", "").replace("&", "§");
		errorMessageInvalidDateFormat = root.getString("error-message-invalid-date-format", "").replace("&", "§");
		errorMessageStartDateEndDateMismatch = root.getString("error-message-start-date-end-date-mismatch", "").replace("&", "§");
	}
	
	private void logInvalidDurationConfigValue(String property, String defaultValue)
	{
		StaffTimesheet.logger.warning(String.format("Invalid duration format for config property: '%s'. Using default value: '%s'. Use duration format hh:mm:ss", 
				property, defaultValue));
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
		String setText = text.replace("%stafftimesheet_current-logged-time%", staffMember.getLoggedTime());
		setText = text.replace("%stafftimesheet_time-goal%", staffMember.getTimeGoal());
		setText = text.replace("%stafftimesheet_staff-member-name%", staffMember.getPlayerName());
		
		return setText;
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
	
	public int getLogCycleDuration()
	{
		return logCycleDuration;
	}
	
	public Duration getUpdateStaffLogsPeriod()
	{
		return updateStaffLogsPeriod;
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
	
	public String getResetStaffMemberLoggedTimeMessage()
	{
		return resetStaffMemberLoggedTimeMessage;
	}
	
	public String getAddLoggedTimeForStaffMemberMessage()
	{
		return addLoggedTimeForStaffMemberMessage;
	}
	
	public String getSubtractLoggedTimeForStaffMemberMessage()
	{
		return subtractLoggedTimeForStaffMemberMessage;
	}
	
	public String getLoadingStaffMemberLogbookMessage()
	{
		return loadingStaffMemberLogbookMessage;
	}
	
	public String getLoadedStaffMemberLogbookMessage()
	{
		return loadedStaffMemberLogbookMessage;
	}
	
	public int getMaxLogRange()
	{
		return maxLogRange;
	}
	
	public String getLogbookTextHeader()
	{
		return logbookTextHeader;
	}
	
	public List<String> getLogbookLoreText(StaffMember staffMember)
	{
		ArrayList<String> setLogbookLoreText = new ArrayList<String>();
		for(String loreTextLine : setLogbookLoreText)
		{
			String line = ChatColor.translateAlternateColorCodes('&', loreTextLine);
			line = setPlaceholders(staffMember, loreTextLine);
			setLogbookLoreText.add(line);
		}
		
		return setLogbookLoreText;
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
	
	public String getErrorMessageStaffMemberDoesNotExist()
	{
		return errorMessageStaffMemberDoesNotExist;
	}
	
	public String getErrorMessageNoStaffLog()
	{
		return errorMessageNoStaffLog;
	}
	
	public String getErrorMessageInvalidDurationFormat()
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