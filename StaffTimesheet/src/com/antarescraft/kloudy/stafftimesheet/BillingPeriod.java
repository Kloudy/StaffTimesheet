package com.antarescraft.kloudy.stafftimesheet;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import com.antarescraft.kloudy.hologuiapi.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.hologuiapi.plugincore.config.ConfigParser;

/**
 * Represents a billing period - Contains a summary of each staff member's logged time during the billing period
 */
public class BillingPeriod 
{
	private Calendar startDate;
	private Calendar endDate;
	
	private HashMap<UUID, StaffMemberSummary> staffMemberSummaries;
	
	public BillingPeriod(Calendar startDate, int weeksInPeriod)
	{
		this.startDate = startDate;

		Duration billingPeriodDuration = TimeFormat.getMinDuration().plusDays(weeksInPeriod * 7);
		
		endDate = Calendar.getInstance();
		endDate.setTimeInMillis((startDate.getTimeInMillis() + billingPeriodDuration.toMillis()));
				
		staffMemberSummaries = new HashMap<UUID, StaffMemberSummary>();
	}
	
	public BillingPeriod(Calendar startDate, Calendar endDate, HashMap<UUID, StaffMemberSummary> staffMemberSummaries)
	{
		this.startDate = startDate;
		this.endDate = endDate;
		
		this.staffMemberSummaries = staffMemberSummaries;
	}
	
	private File getBillingPeriodYamlFile()
	{
		return new File(String.format("plugins/%s/billing-period-history.yml", StaffTimesheet.pluginName));
	}
	
	public void save()
	{
		try 
		{
			ConfigParser.saveObject(getBillingPeriodYamlFile(), "billing-period-history." + getId(), this);
		} 
		catch (IOException e) {}
	}
	
	/*
	 * Removes this BillingPeriod from billing-period-history.yml
	 */
	public void removeFromConfigFile()
	{
		try 
		{
			ConfigParser.saveObject(getBillingPeriodYamlFile(), "billing-period-history." + getId(), null);
		} 
		catch (IOException e){}
	}
	
	/*
	 * Updates the staff member's summary for this billing period
	 */
	public void updateStaffMemberSummary(StaffMember staffMember)
	{
		if(staffMember == null) return;
		
		StaffMemberSummary staffMemberSummary = staffMemberSummaries.get(staffMember.getUUID());
		if(staffMemberSummary == null)
		{	
			staffMemberSummary = new StaffMemberSummary(staffMember);
			staffMemberSummaries.put(staffMember.getUUID(), staffMemberSummary);
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
		return TimeFormat.getDateFormat(startDate) + "-" + TimeFormat.getDateFormat(endDate);
	}
	
	public StaffMemberSummary getStaffMemberSummary(StaffMember staffMember)
	{
		return staffMemberSummaries.get(staffMember.getUUID());
	}
	
	public Calendar getStartDate()
	{
		return startDate;
	}
	
	public Calendar getEndDate()
	{
		return endDate;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof BillingPeriod)
		{
			BillingPeriod billingPeriod = (BillingPeriod)obj;
			
			return billingPeriod.getId().equals(this.getId());
		}
		
		return false;
	}
}