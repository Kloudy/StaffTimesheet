package com.antarescraft.kloudy.stafftimesheet.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.antarescraft.kloudy.stafftimesheet.ShiftManager;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;
import com.earth2me.essentials.UserData;

import net.ess3.api.events.AfkStatusChangeEvent;


public class AfkStatusChangeEventListener implements Listener
{
	private ConfigManager configManager;
	
	public AfkStatusChangeEventListener(ConfigManager configManager)
	{
		this.configManager = configManager;
	}
	
	@EventHandler
	public void afkStatusChangeEvent(AfkStatusChangeEvent event)
	{
		UserData user = (UserData)event.getAffected();
		
		if(user.isAfk())
		{
			Player player = user.getBase();
			StaffMember staffMember = configManager.getStaffMember(player);
			ShiftManager shiftManager = ShiftManager.getInstance();
			if(staffMember != null && shiftManager.onTheClock(staffMember))
			{
				shiftManager.clockOut(staffMember, configManager.getShiftEndLabelAFK());
			}
		}
	}
}