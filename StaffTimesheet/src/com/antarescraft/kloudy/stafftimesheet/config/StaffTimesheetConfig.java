package com.antarescraft.kloudy.stafftimesheet.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import com.antarescraft.kloudy.hologuiapi.plugincore.config.*;

import com.antarescraft.kloudy.hologuiapi.plugincore.exceptions.InvalidDateFormatException;
import com.antarescraft.kloudy.hologuiapi.plugincore.messaging.MessageManager;
import com.antarescraft.kloudy.hologuiapi.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;

import org.bukkit.Bukkit;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Contains all configuration data for StaffTimesheet
 * 
 * This class is instantiated populated by the ConfigParser library
 */
public class StaffTimesheetConfig implements ConfigObject
{	
	private static StaffTimesheetConfig instance;
	
	private StaffMembersConfig staffMemberManager;
	
	private BillingPeriodHistory billingPeriodHistory;
	
	public static void loadConfig(StaffTimesheet staffTimesheet)
	{
		staffTimesheet.saveDefaultConfig();
		staffTimesheet.reloadConfig();
		
		staffTimesheet.loadGUIPages();
		
		System.out.println("gui page count: " + staffTimesheet.getGUIPages().size());
		
		instance = ConfigParser.parse(StaffTimesheet.pluginName, staffTimesheet.getConfig(), StaffTimesheetConfig.class);
	}
	
	public static StaffTimesheetConfig getConfig(StaffTimesheet staffTimesheet)
	{
		if(instance == null)
		{
			instance = ConfigParser.parse(StaffTimesheet.pluginName, staffTimesheet.getConfig(), StaffTimesheetConfig.class);
		}
		
		return instance;
	}
		
	@ConfigElement
	@ConfigProperty(key = "error-messages")
	private ErrorMessages errorMessages;
	
	@ConfigElement
	@ConfigProperty(key = "event-labels")
	private EventLabels eventLabels;
	
	@ConfigElement
	@ConfigProperty(key = "shift-start-stop-messages")
	private ShiftStartStopMessages shiftStartStopMessages;
	
	@ConfigElement
	@ConfigProperty(key = "command-result-messages")
	private CommandResultMessages commandResultMessages;
	
	@OptionalConfigProperty
	@BooleanConfigProperty(defaultValue = false)
	@ConfigProperty(key = "debug-mode")
	public static boolean debugMode;
	
	@IntConfigProperty(defaultValue = 4, maxValue = Integer.MAX_VALUE, minValue = 0)
	@ConfigProperty(key = "billing-period-duration")
	private int billingPeriodDuration;
	
	@ConfigProperty(key = "first-bill-period-start-date")
	private String firstBillPeriodStartDate;
	
	@OptionalConfigProperty
	@ConfigProperty(key = "permissions-plugin")
	public static String permissionsPlugin;
	
	private StaffTimesheetConfig(){}
	
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
	
	private BillingPeriod generateFirstBillingPeriod()
	{
		return new BillingPeriod(getFirstBillPeriodStartDate(), getBillingPeriodDuration());
	}
	
	public BillingPeriod getCurrentBillingPeriod()
	{
		ArrayList<BillingPeriod> billingPeriods = billingPeriodHistory.getBillingPeriodHistory();
		if(billingPeriods.size() == 0)
		{
			billingPeriods.add(generateFirstBillingPeriod());
		}
		
		return billingPeriods.get(billingPeriods.size()-1);//the last billing period in the list will be the current billing period
	}
	
	public void addBillingPeriodToHistory(BillingPeriod billingPeriod)
	{
		billingPeriodHistory.getBillingPeriodHistory().add(billingPeriod);
	}

	/*
	 * Getter Functions
	 */
	
	public StaffMembersConfig getStaffMembersConfig()
	{
		return staffMemberManager;
	}
	
	public EventLabels getEventLabelConfig()
	{
		return eventLabels;
	}
	
	public ErrorMessages getErrorMessageConfig()
	{
		return errorMessages;
	}
	
	public CommandResultMessages getCommandResultMessageConfig()
	{
		return commandResultMessages;
	}
	
	public ShiftStartStopMessages getShiftStartStopMessagesConfig()
	{
		return shiftStartStopMessages;
	}
	
	public BillingPeriodHistory getBillingPeriodHistoryConfig()
	{
		return billingPeriodHistory;
	}
	
	public int getBillingPeriodDuration()
	{
		return billingPeriodDuration;
	}

	@Override
	public void configParseComplete(HashMap<String, Object> passthroughParams) 
	{
		try
		{
			File staffMembersYml = new File(String.format("plugins/%s/staff-members.yml", StaffTimesheet.pluginName));
			YamlConfiguration staffYaml = YamlConfiguration.loadConfiguration(staffMembersYml);
			
			staffMemberManager = ConfigParser.parse(StaffTimesheet.pluginName, staffYaml, StaffMembersConfig.class);
			System.out.println("staff members: " + staffMemberManager.getAllStaffMembers().size());
			
			File billingPeriodHistoryYml = new File(String.format("plugins/%s/billing-period-history.yml", StaffTimesheet.pluginName));
			YamlConfiguration billingPeriodHistoryYaml = YamlConfiguration.loadConfiguration(billingPeriodHistoryYml);
			
			billingPeriodHistory = ConfigParser.parse(StaffTimesheet.pluginName, billingPeriodHistoryYaml, BillingPeriodHistory.class);
		
			System.out.println("billingPeriodHistory: " + billingPeriodHistory.getBillingPeriodHistory().size());
		}
		catch(Exception e){e.printStackTrace();}
	}
}