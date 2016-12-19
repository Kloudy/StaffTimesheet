package com.antarescraft.kloudy.stafftimesheet.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import com.antarescraft.kloudy.plugincore.config.*;

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
public class StaffTimesheetConfig
{	
	private StaffTimesheetConfig()
	{
		try
		{
			File staffMembersYml = new File(String.format("plugins/%s/staff-members.yml", StaffTimesheet.pluginName));
			YamlConfiguration staffYaml = YamlConfiguration.loadConfiguration(staffMembersYml);
			
			staffMemberManager = ConfigParser.parse(staffYaml, StaffMembersConfig.class);
		
			File billingPeriodHistoryYml = new File(String.format("plugins/%s/billing-period-history.yml", StaffTimesheet.pluginName));
			YamlConfiguration billingPeriodHistoryYaml = YamlConfiguration.loadConfiguration(billingPeriodHistoryYml);
			
			System.out.println("Parsing billing period history");
			billingPeriodHistory = ConfigParser.parse(billingPeriodHistoryYaml, BillingPeriodHistory.class);;
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	private StaffMembersConfig staffMemberManager;
	
	private BillingPeriodHistory billingPeriodHistory;
		
	@ConfigElement
	@ConfigProperty(key = "error-messages", note = "List of error messages")
	private ErrorMessages errorMessages;
	
	@ConfigElement
	@ConfigProperty(key = "event-labels", note = "List of event labels that get displayed in a staff member's logbook")
	private EventLabels eventLabels;
	
	@ConfigElement
	@ConfigProperty(key = "shift-start-stop-messages", note = "List of messages that get displayed to staff members when the start or stop their shift")
	private ShiftStartStopMessages shiftStartStopMessages;
	
	@ConfigElement
	@ConfigProperty(key = "command-result-messages", note = "List of messages displayed to the player after running a StaffTimesheet command")
	private CommandResultMessages commandResultMessages;
	
	@OptionalConfigProperty
	@BooleanConfigProperty(defaultValue = false)
	@ConfigProperty(key = "debug-mode", note = "Print debug information in the console")
	public static boolean debugMode;
	
	@IntConfigProperty(defaultValue = 4, maxValue = Integer.MAX_VALUE, minValue = 0)
	@ConfigProperty(key = "billing-period-duration", note = "")
	private int billingPeriodDuration;
	
	@ConfigProperty(key = "first-bill-period-start-date", note = "Start date for the first bill cycle (format: yyyy/mm/dd)")
	private String firstBillPeriodStartDate;
	
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
		ArrayList<BillingPeriod> billingPeriods = billingPeriodHistory.getAllBillingPeriodHistory();
		if(billingPeriods.size() == 0)
		{
			billingPeriods.add(generateFirstBillingPeriod());
		}
		
		return billingPeriods.get(billingPeriods.size()-1);//the last billing period in the list will be the current billing period
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
}