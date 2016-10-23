package com.antarescraft.kloudy.stafftimesheet;

import java.time.Duration;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

import com.antarescraft.kloudy.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;

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
	
	/*
	 * Write data to billing-period-history.yml
	 */
	public void saveToConfigFile()
	{
		ConfigManager.writePropertyToConfigFile("billing-period-history.yml", "billing-period-history." + getId() + 
				".start-date", TimeFormat.getDateFormat(startDate));
		ConfigManager.writePropertyToConfigFile("billing-period-history.yml", "billing-period-history." + getId() + 
				".end-date", TimeFormat.getDateFormat(endDate));
		
		for(StaffMemberSummary summary : staffMemberSummaries.values())
		{
			ConfigManager.writePropertyToConfigFile("billing-period-history.yml", "billing-period-history." + getId() + 
					".staff-member-summaries." + summary.getStaffMemberName() + ".uuid", summary.getStaffMemberUUID().toString());
			
			ConfigManager.writePropertyToConfigFile("billing-period-history.yml", "billing-period-history." + getId() + 
					".staff-member-summaries." + summary.getStaffMemberName() + ".percent-time-logged", summary.getPercentTimeCompleted());
			
			ConfigManager.writePropertyToConfigFile("billing-period-history.yml", "billing-period-history." + getId() + 
					".staff-member-summaries." + summary.getStaffMemberName() + ".time-goal", summary.getTimeGoal());
			
			ConfigManager.writePropertyToConfigFile("billing-period-history.yml", "billing-period-history." + getId() + 
					".staff-member-summaries." + summary.getStaffMemberName() + ".logged-time", summary.getLoggedTime());
		}
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
		
		saveToConfigFile();
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