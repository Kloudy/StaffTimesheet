package com.antarescraft.kloudy.stafftimesheet;

import com.antarescraft.kloudy.hologuiapi.HoloGUIPlugin;
import com.antarescraft.kloudy.hologuiapi.plugincore.config.*;

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
	
	private StaffTimesheetConfig config;
	
	@Override
	public void onEnable()
	{			
		pluginName = getName();
		
		setMinSupportedApiVersion("1.0.5");
		checkMinApiVersion();

		getHoloGUIApi().hookHoloGUIPlugin(this);
		copyResourceConfigs(true);
		
		IOManager.initFileStructure(this);
		
		reloadConfig();
	
		//task that runs every hour to see if we've rolled over into a new billing cycle
		BillingPeriodUpdateTask billingPeriodUpdateTask = new BillingPeriodUpdateTask(config);
		billingPeriodUpdateTask.start(this);
		
		new StaffTimesheetPlaceholders(this, "stafftimesheet", config.getStaffMembersConfig()).hook();
		
		getCommand("staff").setExecutor(new CommandEvent(this, config));
		getServer().getPluginManager().registerEvents(new AfkStatusChangeEventListener(config), this);
		getServer().getPluginManager().registerEvents(new PlayerQuitEventListener(config), this);
		getServer().getPluginManager().registerEvents(new PlayerCommandPreprocessEventListener(config.getStaffMembersConfig()), this);
		getServer().getPluginManager().registerEvents(new PlayerJoinEventListener(config), this);
	}
	
	@Override
	public void onDisable()
	{
		ShiftManager.getInstance().clockOutAll(config.getEventLabelConfig().getShiftEndPluginDisabled());
		this.getHoloGUIApi().destroyGUIPages(this);
	}
	
	@Override
	public void reloadConfig()
	{
		saveDefaultConfig();
		
		super.reloadConfig();
		
		loadGUIPages();
		
		config = ConfigParser.parse(pluginName, getConfig(), StaffTimesheetConfig.class);
	}
}