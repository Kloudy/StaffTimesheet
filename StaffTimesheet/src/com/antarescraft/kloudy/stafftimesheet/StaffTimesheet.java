package com.antarescraft.kloudy.stafftimesheet;

import java.util.logging.Logger;

import com.antarescraft.kloudy.hologuiapi.HoloGUIPlugin;
import com.antarescraft.kloudy.hologuiapi.PlayerData;
import com.antarescraft.kloudy.stafftimesheet.events.AfkStatusChangeEventListener;
import com.antarescraft.kloudy.stafftimesheet.events.CommandEvent;
import com.antarescraft.kloudy.stafftimesheet.events.PlayerCommandPreprocessEventListener;
import com.antarescraft.kloudy.stafftimesheet.events.PlayerJoinEventListener;
import com.antarescraft.kloudy.stafftimesheet.events.PlayerQuitEventListener;
import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;
import com.antarescraft.kloudy.stafftimesheet.util.IOManager;

public class StaffTimesheet extends HoloGUIPlugin
{
	public static Logger logger;
	public static boolean debugMode;
	public static String pluginName;
	
	private ConfigManager configManager;
	
	@Override
	public void onEnable()
	{	
		setMinSupportedApiVersion("1.0.2");
		checkMinApiVersion();
		
		logger = this.getLogger();
		
		saveDefaultConfig();
		
		pluginName = getName();
			
		getHoloGUIApi().hookHoloGUIPlugin(this);
		copyResourceConfigs(true);
		loadGUIPages();
		
		IOManager.initFileStructure(this);
		
		configManager = new ConfigManager(this);
		configManager.loadConfigValues();
	
		//task that runs every hour to see if we've rolled over into a new billing cycle
		BillingPeriodUpdateTask billingPeriodUpdateTask = new BillingPeriodUpdateTask(configManager);
		billingPeriodUpdateTask.start(this);
		
		new StaffTimesheetPlaceholders(this, "stafftimesheet", configManager).hook();
		
		getCommand("staff").setExecutor(new CommandEvent(this, configManager));
		getServer().getPluginManager().registerEvents(new AfkStatusChangeEventListener(configManager), this);
		getServer().getPluginManager().registerEvents(new PlayerQuitEventListener(configManager), this);
		getServer().getPluginManager().registerEvents(new PlayerCommandPreprocessEventListener(configManager), this);
		getServer().getPluginManager().registerEvents(new PlayerJoinEventListener(configManager), this);
	}
	
	@Override
	public void onDisable()
	{
		ShiftManager.getInstance().clockOutAll(configManager.getShiftEndLabelPluginDisabled());

		destroyPlayerGUIPages();
	}
	
	public void destroyPlayerGUIPages()
	{
		for(PlayerData playerData : PlayerData.getAllPlayerData())
		{
			if(playerData.getPlayerGUIPage() != null) playerData.getPlayerGUIPage().destroy();
		}
	}
}