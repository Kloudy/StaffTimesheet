package com.antarescraft.kloudy.stafftimesheet;

import com.antarescraft.kloudy.hologui.HoloGUIPlugin;
import com.antarescraft.kloudy.stafftimesheet.events.CommandEvent;
import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;

public class StaffTimesheet extends HoloGUIPlugin
{
	@Override
	public void onEnable()
	{
		saveDefaultConfig();
		
		ConfigManager configManager = new ConfigManager(this);
		configManager.loadConfigValues();
		
		getCommand("staff").setExecutor(new CommandEvent(this, configManager));
	}
	
	@Override
	public void onDisable()
	{
		
	}
}
