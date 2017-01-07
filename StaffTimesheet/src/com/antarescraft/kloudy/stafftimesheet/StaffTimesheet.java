package com.antarescraft.kloudy.stafftimesheet;

import com.antarescraft.kloudy.hologuiapi.HoloGUIPlugin;

import com.antarescraft.kloudy.stafftimesheet.config.StaffTimesheetConfig;
import com.antarescraft.kloudy.stafftimesheet.events.AfkStatusChangeEventListener;
import com.antarescraft.kloudy.stafftimesheet.events.CommandEvent;
import com.antarescraft.kloudy.stafftimesheet.events.PlayerCommandPreprocessEventListener;
import com.antarescraft.kloudy.stafftimesheet.events.PlayerJoinEventListener;
import com.antarescraft.kloudy.stafftimesheet.events.PlayerQuitEventListener;
import com.antarescraft.kloudy.stafftimesheet.util.IOManager;

public class StaffTimesheet extends HoloGUIPlugin
{
	public static String pluginName;
		
	@Override
	public void onEnable()
	{			
		pluginName = getName();
		
		setMinSupportedApiVersion("1.0.7");
		checkMinApiVersion();

		getHoloGUIApi().hookHoloGUIPlugin(this);
		copyResourceConfigs(true);
		
		IOManager.initFileStructure(this);
		
		StaffTimesheetConfig.loadConfig(this);
	
		//task that runs every hour to see if we've rolled over into a new billing cycle
		BillingPeriodUpdateTask billingPeriodUpdateTask = new BillingPeriodUpdateTask(this);
		billingPeriodUpdateTask.start(this);
		
		new StaffTimesheetPlaceholders(this, "stafftimesheet", StaffTimesheetConfig.getConfig(this).getStaffMembersConfig()).hook();
		
		getCommand("staff").setExecutor(new CommandEvent(this));
		getServer().getPluginManager().registerEvents(new AfkStatusChangeEventListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerQuitEventListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerCommandPreprocessEventListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerJoinEventListener(this), this);
	}
	
	@Override
	public void onDisable()
	{
		ShiftManager.getInstance().clockOutAll(StaffTimesheetConfig.getConfig(this).getEventLabelConfig().getShiftEndPluginDisabled());
		this.getHoloGUIApi().destroyGUIPages(this);
	}
}