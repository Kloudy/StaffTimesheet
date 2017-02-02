package com.antarescraft.kloudy.stafftimesheet.config;

import java.util.ArrayList;

import com.antarescraft.kloudy.hologuiapi.plugincore.config.*;
import com.antarescraft.kloudy.hologuiapi.plugincore.config.annotations.*;

/**
 * Contains all BillingPeriod objects loaded from config
 * 
 * This class is instantiated populated by the ConfigParser library
 */
public class BillingPeriodHistory  implements ConfigObject
{
	private BillingPeriodHistory(){}
	
	@ConfigElementList
	@ConfigProperty(key = "billing-period-history")
	private ArrayList<BillingPeriod> billingPeriodHistory;
	
	public ArrayList<BillingPeriod> getBillingPeriodHistory()
	{
		return billingPeriodHistory;
	}

	@Override
	public void configParseComplete(PassthroughParams params) {}
}