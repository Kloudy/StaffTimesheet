package com.antarescraft.kloudy.stafftimesheet.config;

import java.util.ArrayList;
import java.util.HashMap;

import com.antarescraft.kloudy.hologuiapi.plugincore.config.ConfigProperty;
import com.antarescraft.kloudy.stafftimesheet.BillingPeriod;

/**
 * Contains all BillingPeriod objects loaded from config
 * 
 * This class is instantiated populated by the ConfigParser library
 */
public class BillingPeriodHistory 
{
	private BillingPeriodHistory(){}
	
	@ConfigProperty(key = "billing-period-history", note = "")
	private HashMap<String, BillingPeriod> billingPeriodHistory;
	
	public ArrayList<BillingPeriod> getAllBillingPeriodHistory()
	{
		ArrayList<BillingPeriod> history = new ArrayList<BillingPeriod>();
		for(BillingPeriod billingPeriod : billingPeriodHistory.values())
		{
			history.add(billingPeriod);
		}
		
		return history;
	}
}