package com.antarescraft.kloudy.stafftimesheet.datamodels;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologui.HoloGUIPlugin;
import com.antarescraft.kloudy.hologui.guicomponents.GUIPage;
import com.antarescraft.kloudy.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.stafftimesheet.BillingPeriod;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;

public class BillingPeriodHistoryPageModel extends BaseStaffTimesheetPageModel
{
	private ArrayList<BillingPeriod> billingPeriodHistory;
	
	private int page;
	
	public BillingPeriodHistoryPageModel(HoloGUIPlugin plugin, GUIPage guiPage, Player player, StaffMember staffMember)
	{
		super(plugin, guiPage, player, staffMember);
				
		billingPeriodHistory = ConfigManager.getAllBillingPeriods();//read all billing history data from 'staff-member-billing-period-history.yml'
	
		page = 0;
	}
	
	public String startDate()
	{
		BillingPeriod billingPeriod = getBillingPeriod();
		return TimeFormat.getDateFormat(billingPeriod.getStartDate());
	}
	
	public String endDate()
	{
		BillingPeriod billingPeriod = getBillingPeriod();
		return TimeFormat.getDateFormat(billingPeriod.getEndDate());
	}
	
	/*
	 * Get billing period for current page. Most recent billing periods are at the end of the list.
	 */
	private BillingPeriod getBillingPeriod()
	{
		return billingPeriodHistory.get(billingPeriodHistory.size() - 1 - page);
	}
}