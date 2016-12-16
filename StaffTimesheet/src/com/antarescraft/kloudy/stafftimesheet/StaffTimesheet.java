package com.antarescraft.kloudy.stafftimesheet;

import java.io.IOException;

import com.antarescraft.kloudy.hologuiapi.HoloGUIPlugin;
import com.antarescraft.kloudy.hologuiapi.PlayerData;
import com.antarescraft.kloudy.plugincore.config.*;

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
		
		setMinSupportedApiVersion("1.0.2");
		checkMinApiVersion();
		
		saveDefaultConfig();

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
		destroyPlayerGUIPages();
	}
	
	@Override
	public void reloadConfig()
	{
		super.reloadConfig();
		
		loadGUIPages();
		
		try 
		{
			config = ConfigParser.parse(getConfig(), 
					StaffTimesheetConfig.class, String.format("plugins/%s/config.yml", getName()), 45);
		} 
		catch (ConfigurationParseException | IOException e) {e.printStackTrace();}
	}
	
	public void destroyPlayerGUIPages()
	{
		for(PlayerData playerData : PlayerData.getAllPlayerData())
		{
			if(playerData.getPlayerGUIPage() != null) playerData.getPlayerGUIPage().destroy();
		}
	}
}