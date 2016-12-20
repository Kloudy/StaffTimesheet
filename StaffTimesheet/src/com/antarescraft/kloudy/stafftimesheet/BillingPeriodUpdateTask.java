package com.antarescraft.kloudy.stafftimesheet;

import java.util.Calendar;

import org.bukkit.scheduler.BukkitRunnable;

import com.antarescraft.kloudy.hologuiapi.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.stafftimesheet.config.BillingPeriod;
import com.antarescraft.kloudy.stafftimesheet.config.StaffTimesheetConfig;

/**
 * Repeating task that checks to see if we've rolled over into a new billing period
 */

public class BillingPeriodUpdateTask extends BukkitRunnable
{
	private StaffTimesheetConfig config;
	private BillingPeriod currentBillingPeriod;
	private ShiftManager shiftManager;
	
	public BillingPeriodUpdateTask(StaffTimesheetConfig config)
	{
		this.config = config;
		
		currentBillingPeriod = config.getCurrentBillingPeriod();
		currentBillingPeriod.save();
		
		shiftManager = ShiftManager.getInstance();
		shiftManager.setCurrentBillingPeriod(currentBillingPeriod);
	}
	
	/*
	 * Runs this task every hour
	 */
	public void start(StaffTimesheet plugin)
	{
		this.runTaskTimer(plugin, 0, 72000);
	}
	
	@Override
	public void run()
	{
		Calendar endDate = currentBillingPeriod.getEndDate();
		System.out.print(TimeFormat.getDateFormat(currentBillingPeriod.getEndDate()));
		Calendar now = Calendar.getInstance();
		if(now.compareTo(endDate) >= 0)//we've rolled over into a new billing period
		{
			BillingPeriod newBillingPeriod = new BillingPeriod(now, config.getBillingPeriodDuration());
			newBillingPeriod.save();
			
			shiftManager.setCurrentBillingPeriod(newBillingPeriod);
			
			config.getStaffMembersConfig().resetAllStaffMemberTime();//resets all staff member time for the new billing period
		}
	}
}