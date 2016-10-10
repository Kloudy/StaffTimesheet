package com.antarescraft.kloudy.stafftimesheet;

import java.util.logging.Logger;

import org.bukkit.Bukkit;

import com.antarescraft.kloudy.hologui.HoloGUIPlugin;
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
		logger = this.getLogger();
		
		saveDefaultConfig();
		
		pluginName = getName();
				
		getHoloGUI().getHoloGUIPluginManager().hookHoloGUIPlugin(this);
		loadGUIContainersFromYaml(Bukkit.getConsoleSender());
		
		IOManager.initFileStructure(this);
		
		configManager = new ConfigManager(this);
		configManager.loadConfigValues();
		
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
		ShiftManager.getInstance().clockOutAll(ShiftEndReason.PLUGIN_DISABLED);
	}
}