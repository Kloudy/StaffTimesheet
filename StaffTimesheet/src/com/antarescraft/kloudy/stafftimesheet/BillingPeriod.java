package com.antarescraft.kloudy.stafftimesheet;

import java.util.ArrayList;
import java.util.Calendar;

import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;

/**
 * Represents a billing period - Contains data about activities of each staff member during the billing period
 */
public class BillingPeriod 
{
	private Calendar startDate;
	private Calendar endDate;
	
	private ArrayList<StaffMemberSummary> staffMemberSummaries;
	
	private BillingPeriod(Calendar startDate, Calendar endDate)
	{
		this.startDate = startDate;
		this.endDate = endDate;
		
		staffMemberSummaries = new ArrayList<StaffMemberSummary>();
	}
	
	public static BillingPeriod generateBillPeriodHistory(ConfigManager configManager)
	{
		return null;
	}
	
	/*
	 * Getter Functions
	 */
	
	public StaffMemberSummary getStaffMemberSummary(StaffMember staffMember)
	{
		for(StaffMemberSummary staffMemberSummary : staffMemberSummaries)
		{
			
		}
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