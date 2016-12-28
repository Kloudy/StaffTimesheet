package com.antarescraft.kloudy.stafftimesheet.config;

import java.util.ArrayList;

import com.antarescraft.kloudy.hologuiapi.plugincore.config.*;

/**
 * Contains all BillingPeriod objects loaded from config
 * 
 * This class is instantiated populated by the ConfigParser library
 */
public class BillingPeriodHistory 
{
	private BillingPeriodHistory(){}
	
	@ConfigElementList
	@ConfigProperty(key = "billing-period-history")
	private ArrayList<BillingPeriod> billingPeriodHistory;
	
	public ArrayList<BillingPeriod> getBillingPeriodHistory()
	{
		return billingPeriodHistory;
	}
}