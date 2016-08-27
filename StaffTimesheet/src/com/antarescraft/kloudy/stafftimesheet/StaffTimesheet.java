package com.antarescraft.kloudy.stafftimesheet;

import com.antarescraft.kloudy.hologui.HoloGUIPlugin;
import com.antarescraft.kloudy.stafftimesheet.events.CommandEvent;

public class StaffTimesheet extends HoloGUIPlugin
{
	@Override
	public void onEnable()
	{
		getCommand("staff").setExecutor(new CommandEvent(this));
	}
	
	@Override
	public void onDisable()
	{
		
	}
}
