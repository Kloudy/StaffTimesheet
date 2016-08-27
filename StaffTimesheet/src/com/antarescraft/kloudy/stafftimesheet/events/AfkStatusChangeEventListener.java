package com.antarescraft.kloudy.stafftimesheet.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.antarescraft.kloudy.stafftimesheet.ShiftManager;
import com.earth2me.essentials.UserData;

import net.ess3.api.events.AfkStatusChangeEvent;

public class AfkStatusChangeEventListener implements Listener
{
	@EventHandler
	public void afkStatusChangeEvent(AfkStatusChangeEvent event)
	{
		UserData user = (UserData)event.getAffected();
		if(user.isAfk())
		{
			ShiftManager shiftManager = ShiftManager.getInstance();
			shiftManager.endShift(user.getBase());
		}
	}
}