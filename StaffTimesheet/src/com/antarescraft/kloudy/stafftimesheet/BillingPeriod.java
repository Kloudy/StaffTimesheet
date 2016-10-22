package com.antarescraft.kloudy.stafftimesheet;

import java.time.Duration;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import com.antarescraft.kloudy.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;

/**
 * Represents a billing period - Contains data about activities of each staff member during the billing period
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
	
	public void saveToConfigFile()
	{
		ConfigManager.writePropertyToConfigFile("billing-period-history.yml", "billing-period-history." + getId() + 
				"start-date", TimeFormat.getDateFormat(startDate));
		ConfigManager.writePropertyToConfigFile("billing-period-history.yml", "billing-period-history." + getId() + 
				"end-date", TimeFormat.getDateFormat(endDate));
		
		//write staff member summaries
		for(StaffMemberSummary summary : staffMemberSummaries.values())
		{
			ConfigManager.writePropertyToConfigFile("billing-period-history.yml", "billing-period-history." + getId() + 
					"staff-summaries." + summary.getStaffMemberName() + ".uuid", summary.getStaffMemberUUID());
			
			ConfigManager.writePropertyToConfigFile("billing-period-history.yml", "billing-period-history." + getId() + 
					"staff-summaries." + summary.getStaffMemberName() + ".percent-time-logged", summary.getPercentTimeLogged());
			
			ConfigManager.writePropertyToConfigFile("billing-period-history.yml", "billing-period-history." + getId() + 
					"staff-summaries." + summary.getStaffMemberName() + ".logged-time", summary.getTimeLogged());
		}
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
}