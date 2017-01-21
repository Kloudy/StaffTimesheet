package com.antarescraft.kloudy.stafftimesheet.config;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Calendar;
import java.util.HashMap;

import org.bukkit.Bukkit;

import com.antarescraft.kloudy.hologuiapi.plugincore.exceptions.InvalidDateFormatException;
import com.antarescraft.kloudy.hologuiapi.plugincore.messaging.MessageManager;
import com.antarescraft.kloudy.hologuiapi.plugincore.time.TimeFormat;

import com.antarescraft.kloudy.hologuiapi.plugincore.config.*;
import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;

/**
 * Represents a billing period - Contains a summary of each staff member's logged time during the billing period
 * 
 * This class is instantiated populated by the ConfigParser library
 */

public class BillingPeriod implements ConfigObject
{	
	@ConfigElementKey
	private String id;
	
	@ConfigProperty(key = "start-date")
	private String startDateString;
	
	@ConfigProperty(key = "end-date")
	private String endDateString;
	
	@ConfigElementMap
	@ConfigProperty(key = "staff-member-summaries")
	private HashMap<String, StaffMemberSummary> staffMemberSummaries;
	
	public BillingPeriod(){}
	
	public BillingPeriod(Calendar startDate, int weeksInPeriod)
	{
		startDateString = TimeFormat.getDateFormat(startDate);

		Duration billingPeriodDuration = TimeFormat.getMinDuration().plusDays(weeksInPeriod * 7);
		
		Calendar endDate = Calendar.getInstance();
		endDate.setTimeInMillis((startDate.getTimeInMillis() + billingPeriodDuration.toMillis()));
		endDateString = TimeFormat.getDateFormat(endDate);
		
		id = startDateString + "-" + endDateString;
		
		staffMemberSummaries = new HashMap<String, StaffMemberSummary>();
	}
	
	private File getBillingPeriodYamlFile()
	{
		return new File(String.format("plugins/%s/billing-period-history.yml", StaffTimesheet.pluginName));
	}
	
	public void save()
	{
		try 
		{
			ConfigParser.saveObject(StaffTimesheet.pluginName, getBillingPeriodYamlFile(), "billing-period-history." + getId(), this);
		} 
		catch (IOException e)
		{
			MessageManager.error(Bukkit.getConsoleSender(), String.format("An error occured while saving class %s to yml", this.getClass().getName()));
		}
	}
	
	/*
	 * Removes this BillingPeriod from billing-period-history.yml
	 */
	public void removeFromConfigFile()
	{
		try 
		{
			ConfigParser.saveObject(StaffTimesheet.pluginName, getBillingPeriodYamlFile(), "billing-period-history." + getId(), null);
		} 
		catch (IOException e)
		{
			MessageManager.error(Bukkit.getConsoleSender(), String.format("An error occured while removinb class %s from yml"));
		}
	}
	
	/*
	 * Updates the staff member's summary for this billing period
	 */
	public void updateStaffMemberSummary(StaffMember staffMember)
	{
		if(staffMember == null) return;
				
		StaffMemberSummary staffMemberSummary = staffMemberSummaries.get(staffMember.getUUID().toString());
		if(staffMemberSummary == null)
		{	
			staffMemberSummary = new StaffMemberSummary(staffMember);
			staffMemberSummaries.put(staffMember.getUUID().toString(), staffMemberSummary);
		}
		
		staffMemberSummary.setPercentTimeCompleted(staffMember.getPercentageTimeCompleted());
		staffMemberSummary.setTimeGoal(staffMember.getTimeGoal());
		staffMemberSummary.setLoggedTime(staffMember.getLoggedTime());
		
		save();
	}

	/*
	 * Getter Functions
	 */
	
	public String getId()
	{
		return id;
	}
	
	public StaffMemberSummary getStaffMemberSummary(StaffMember staffMember)
	{
		return staffMemberSummaries.get(staffMember.getUUID().toString());
	}
	
	public Calendar getStartDate()
	{
		try 
		{
			return TimeFormat.parseDateFormat(startDateString);
		}
		catch (InvalidDateFormatException e){}
		
		return null;
	}
	
	public Calendar getEndDate()
	{
		try 
		{
			return TimeFormat.parseDateFormat(endDateString);
		}
		catch (InvalidDateFormatException e){}
		
		return null;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof BillingPeriod)
		{
			BillingPeriod billingPeriod = (BillingPeriod)obj;
			
			return billingPeriod.getId().equals(id);
		}
		
		return false;
	}

	@Override
	public void configParseComplete(){}
}