package com.antarescraft.kloudy.stafftimesheet.util;

import java.io.File;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.antarescraft.kloudy.hologuiapi.plugincore.exceptions.InvalidDateFormatException;
import com.antarescraft.kloudy.hologuiapi.plugincore.exceptions.InvalidDurationFormatException;
import com.antarescraft.kloudy.hologuiapi.plugincore.messaging.MessageManager;
import com.antarescraft.kloudy.hologuiapi.plugincore.time.TimeFormat;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.stafftimesheet.BillingPeriod;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.StaffMemberSummary;
import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;

public class ConfigManager
{	
	private static StaffTimesheet staffTimesheetPlugin;

	private int billingPeriodDuration;
	private String firstBillPeriodStartDate;
	
	private static ArrayList<BillingPeriod> billingPeriodHistory;
	
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
	private String shiftEndLabelPluginDisabled;
	
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
		
		billingPeriodDuration = root.getInt("billing-period-duration", 4);
		
		firstBillPeriodStartDate = root.getString("first-bill-period-start-date");
		
		try
		{
			updateStaffLogsPeriod = TimeFormat.parseDurationFormat(root.getString("update-staff-logs-period", "00:01:00"));
		}
		catch(InvalidDurationFormatException e)
		{
			this.logInvalidDurationConfigValue("update-staff-logs-period", "00:01:00");
			try 
			{
				updateStaffLogsPeriod = TimeFormat.parseDurationFormat("00:01:00");
			} catch (InvalidDurationFormatException de) {}
		}
		
		shiftEndAFKMessage = setFormattingCodes(root.getString("shift-end-afk-message", ""));
		shiftEndClockoutMessage = setFormattingCodes(root.getString("shift-end-clockout-message", ""));
		shiftStartMessage = setFormattingCodes(root.getString("shift-start-message", ""));
		resetStaffMemberLoggedTimeMessage = setFormattingCodes(root.getString("reset-staff-member-logged-time-message", ""));
		addLoggedTimeForStaffMemberMessage = setFormattingCodes(root.getString("add-logged-time-for-staff-member-message", ""));
		subtractLoggedTimeForStaffMemberMessage = setFormattingCodes(root.getString("subtract-logged-time-for-staff-member-message", ""));
		loadingStaffMemberLogbookMessage = setFormattingCodes(root.getString("loading-staff-member-logbook-message", ""));
		loadedStaffMemberLogbookMessage = setFormattingCodes(root.getString("loaded-staff-member-logbook-message", ""));
		maxLogRange = root.getInt("max-log-range", 365);
		logbookTextHeader = setFormattingCodes(root.getString("logbook-text-header", ""));
		logbookLoreText = root.getStringList("logbook-lore-text");
		shiftStartLabel = setFormattingCodes(root.getString("shift-start-label", ""));
		shiftEndLabelAFK = setFormattingCodes(root.getString("shift-end-label-afk"));
		shiftEndLabelDisconnected = setFormattingCodes(root.getString("shift-end-label-disconnected", ""));
		shiftEndLabelClockedOut = setFormattingCodes(root.getString("shift-end-label-clockout", ""));
		shiftEndLabelPluginDisabled =  setFormattingCodes(root.getString("shift-end-label-plugin-disabled", ""));
		
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
				boolean startShiftOnLogin = staffMembersSection.getBoolean(playerName + ".start-shift-on-login", false);
				
				Duration timeGoalDuration = null;
				Duration loggedTimeDuration = null;
				
				//check 'time-goal-duration' format
				try
				{
					timeGoalDuration = TimeFormat.parseDurationFormat(timeGoal);
				}
				catch(InvalidDurationFormatException e)
				{
					logInvalidDurationConfigValue(playerName + ".time-goal", "15:00:00");
					
					try 
					{
						timeGoalDuration = TimeFormat.parseDurationFormat("15:00:00");
					} catch (InvalidDurationFormatException e1) {}
				}
				
				//check 'logged-time' format
				try
				{
					loggedTimeDuration = TimeFormat.parseDurationFormat(loggedTime);
				}
				catch(InvalidDurationFormatException e)
				{
					logInvalidDurationConfigValue(playerName + ".logged-time", "00:00:00");
					
					try 
					{
						loggedTimeDuration = TimeFormat.parseDurationFormat("00:00:00");
					} catch (InvalidDurationFormatException e1) {}
				}
				
				
				StaffMember staffMember = new StaffMember(playerName, playerUUID, clockInPermission,
						timeGoalDuration, rankTitle, loggedTimeDuration, startShiftOnLogin , superAdmin);
				
				
				staffMembers.put(UUID.fromString(playerUUID), staffMember);
			}
		}
		
		errorMessageDurationUnderflow = setFormattingCodes(root.getString("error-message-negative-time", ""));
		errorMessageDurationOverflow = setFormattingCodes(root.getString("error-message-duration-overflow", ""));
		errorMessageNotStaff = setFormattingCodes(root.getString("error-message-not-staff", ""));
		errorMessageNotClockedIn = setFormattingCodes(root.getString("error-message-not-clocked-in", ""));
		errorMessageAlreadyClockedIn = setFormattingCodes(root.getString("error-message-already-clocked-in", ""));
		errorMessageStaffMemberDoesNotExist = setFormattingCodes(root.getString("error-message-staff-member-does-not-exist", ""));
		errorMessageNoStaffLog = setFormattingCodes(root.getString("error-message-no-staff-log", ""));
		errorMessageInvalidDurationFormat = setFormattingCodes(root.getString("error-message-invalid-duration-format", ""));
		errorMessageInvalidDateFormat = setFormattingCodes(root.getString("error-message-invalid-date-format", ""));
		errorMessageStartDateEndDateMismatch = setFormattingCodes(root.getString("error-message-start-date-end-date-mismatch", ""));
	}
	
	private String setFormattingCodes(String text)
	{
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	
	private void logInvalidDurationConfigValue(String property, String defaultValue)
	{
		StaffTimesheet.logger.warning(String.format("Invalid duration format for config property: '%s'. Using default value: '%s'. Use duration format hh:mm:ss", 
				property, defaultValue));
	}
	
	public static void writePropertyToConfigFile(String yamlRelativeFilePath, String path, Object value)
	{
		try
		{	
			File configFile = new File("plugins/" + staffTimesheetPlugin.getName() + "/" + yamlRelativeFilePath);
			YamlConfiguration yaml = YamlConfiguration.loadConfiguration(configFile);
			yaml.set(path, value);
			yaml.save(configFile);

		}catch(Exception e){
			MessageManager.error(Bukkit.getConsoleSender(),
				String.format("[%s]Error saving values to the config file. Does the file still exist?", staffTimesheetPlugin.getName()));}
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
	
	public Collection<StaffMember> getAllStaffMembers()
	{
		return staffMembers.values();
	}
	
	private BillingPeriod generateFirstBillingPeriod()
	{
		return new BillingPeriod(getFirstBillPeriodStartDate(), getBillingPeriodDuration());
	}
	
	public BillingPeriod getCurrentBillingPeriod()
	{
		ArrayList<BillingPeriod> billingPeriods = getAllBillingPeriods();
		if(billingPeriods.size() == 0)
		{
			billingPeriods.add(generateFirstBillingPeriod());
		}
		
		return billingPeriods.get(billingPeriods.size()-1);//the last billing period in the list will be the current billing period
	}
	
	public void resetAllStaffMemberTime()
	{
		for(StaffMember staffMember : staffMembers.values())
		{
			staffMember.resetLoggedTime();
		}
	}
	
	/*
	 * Reads staff-member-billing-period-history.yml and returns all billing period history
	 */
	public static ArrayList<BillingPeriod> getAllBillingPeriods()
	{
		billingPeriodHistory = new ArrayList<BillingPeriod>();
		
		File billingPeriodHistoryYmlFile = new File(String.format("plugins/%s/billing-period-history.yml", staffTimesheetPlugin.getName()));
		if(!billingPeriodHistoryYmlFile.exists())
		{
			IOManager.initFileStructure(staffTimesheetPlugin);
		}
		
		//parse billing-period-history.yml
		YamlConfiguration billingPeriodHistoryYaml = YamlConfiguration.loadConfiguration(billingPeriodHistoryYmlFile);
		ConfigurationSection billingPeriodHistorySection = billingPeriodHistoryYaml.getConfigurationSection("billing-period-history");
		if(billingPeriodHistorySection != null)
		{
			for(String key : billingPeriodHistorySection.getKeys(false))
			{
				ConfigurationSection billingPeriodSection = billingPeriodHistorySection.getConfigurationSection(key);
				try
				{
					Calendar startDate = TimeFormat.parseDateFormat(billingPeriodSection.getString("start-date"));
					Calendar endDate = TimeFormat.parseDateFormat(billingPeriodSection.getString("end-date"));
					
					ConfigurationSection staffMemberSummariesSection = billingPeriodSection.getConfigurationSection("staff-member-summaries");
					HashMap<UUID, StaffMemberSummary> staffMemberSummaries = new HashMap<UUID, StaffMemberSummary>();
					if(staffMemberSummariesSection != null)
					{
						for(String staffMemberName : staffMemberSummariesSection.getKeys(false))
						{
							ConfigurationSection summarySection = staffMemberSummariesSection.getConfigurationSection(staffMemberName);
							
							UUID uuid = UUID.fromString(summarySection.getString("uuid"));
							double percentTimeLogged = summarySection.getDouble("percent-time-logged");
							Duration timeGoal = TimeFormat.parseDurationFormat(summarySection.getString("time-goal"));
							Duration timeLogged = TimeFormat.parseDurationFormat(summarySection.getString("logged-time"));
							
							staffMemberSummaries.put(uuid, new StaffMemberSummary(staffMemberName, uuid, percentTimeLogged, timeGoal, timeLogged));
						}
					}
					
					billingPeriodHistory.add(new BillingPeriod(startDate, endDate, staffMemberSummaries));
				}
				catch(InvalidDateFormatException e){e.printStackTrace();}
				catch(InvalidDurationFormatException e){e.printStackTrace();}
			}
		}
		
		return billingPeriodHistory;
	}
	
	public int getBillingPeriodDuration()
	{
		return billingPeriodDuration;
	}
	
	public Calendar getFirstBillPeriodStartDate()
	{	
		Calendar date = null;
		try 
		{
			date = TimeFormat.parseDateFormat(firstBillPeriodStartDate);
		} catch (InvalidDateFormatException e) {}
		
		if(date == null)
		{
			MessageManager.error(Bukkit.getConsoleSender(), "Invalid Config Value: current-bill-period-start-date");
		}
		
		return date;
	}
	
	public Duration getUpdateStaffLogsPeriod()
	{
		return updateStaffLogsPeriod;
	}
	
	public String getShiftEndAFKMessage(StaffMember staffMember)
	{
		return PlaceholderAPI.setPlaceholders(staffMember.getPlayer(), shiftEndAFKMessage);
	}
	
	public String getShiftEndClockoutMessage(StaffMember staffMember)
	{
		return PlaceholderAPI.setPlaceholders(staffMember.getPlayer(), shiftEndClockoutMessage);
	}
	
	public String getShiftStartMessage(StaffMember staffMember)
	{
		return PlaceholderAPI.setPlaceholders(staffMember.getPlayer(), shiftStartMessage);
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
			String line = setFormattingCodes(loreTextLine);
			line = PlaceholderAPI.setPlaceholders(staffMember.getPlayer(), line);
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
	
	public String getShiftEndLabelClockOut()
	{
		return shiftEndLabelClockedOut;
	}
	
	public String getShiftEndLabelPluginDisabled()
	{
		return shiftEndLabelPluginDisabled;
	}
	
	public String getErrorMessageDurationUnderflow()
	{
		return errorMessageDurationUnderflow;
	}
	
	public String getErrorMessageDurationOverflow()
	{
		return errorMessageDurationOverflow;
	}
	
	public String getErrorMessageNotStaff()
	{
		return errorMessageNotStaff;
	}
	
	public String getErrorMessageNotClockedIn()
	{
		return errorMessageNotClockedIn;
	}
	
	public String getErrorMessageAlreadyClockedIn()
	{
		return errorMessageAlreadyClockedIn;
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