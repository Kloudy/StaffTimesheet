package com.antarescraft.kloudy.stafftimesheet.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.antarescraft.kloudy.stafftimesheet.ShiftManager;
import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;
import com.antarescraft.kloudy.stafftimesheet.config.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.config.StaffTimesheetConfig;

public class PlayerCommandPreprocessEventListener implements Listener
{
	private StaffTimesheet staffTimesheet;
	
	public PlayerCommandPreprocessEventListener(StaffTimesheet staffTimesheet)
	{
		this.staffTimesheet = staffTimesheet;
	}
	
	@EventHandler
	public void playerCommandPreprocessEvent(PlayerCommandPreprocessEvent event)
	{
		Player player = event.getPlayer();
		
		StaffMember staffMember = StaffTimesheetConfig.getConfig(staffTimesheet).getStaffMembersConfig().getStaffMember(player);
		if(staffMember != null && ShiftManager.getInstance().onTheClock(staffMember))
		{
			staffMember.logEntry(event.getMessage());
		}
	}
}