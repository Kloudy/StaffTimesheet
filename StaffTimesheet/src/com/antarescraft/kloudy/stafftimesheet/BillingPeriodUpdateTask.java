package com.antarescraft.kloudy.stafftimesheet;

import java.util.Calendar;

import org.bukkit.scheduler.BukkitRunnable;

import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;

/**
 * Repeating task that checks to see if we've rolled over into a new billing period
 */

public class BillingPeriodUpdateTask extends BukkitRunnable
{
	private ConfigManager configManager;
	private BillingPeriod currentBillingPeriod;
	private ShiftManager shiftManager;
	
	public BillingPeriodUpdateTask(ConfigManager configManager)
	{
		this.configManager = configManager;
		
		currentBillingPeriod = configManager.getCurrentBillingPeriod();
		currentBillingPeriod.saveToConfigFile();
		
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
		Calendar now = Calendar.getInstance();
		if(now.compareTo(endDate) >= 0)//we've rolled over into a new billing period
		{
			BillingPeriod newBillingPeriod = new BillingPeriod(now, configManager.getBillingPeriodDuration());
			newBillingPeriod.saveToConfigFile();
			
			shiftManager.setCurrentBillingPeriod(newBillingPeriod);
			
			configManager.resetAllStaffMemberTime();//resets all staff member time for the new billing period
		}
	}
}