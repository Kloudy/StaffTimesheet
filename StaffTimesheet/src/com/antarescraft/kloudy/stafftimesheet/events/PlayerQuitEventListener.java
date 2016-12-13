package com.antarescraft.kloudy.stafftimesheet.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.antarescraft.kloudy.stafftimesheet.ShiftManager;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.config.StaffTimesheetConfig;

public class PlayerQuitEventListener implements Listener
{
	private StaffTimesheetConfig configManager;
	
	public PlayerQuitEventListener(StaffTimesheetConfig configManager)
	{
		this.configManager = configManager;
	}
	
	@EventHandler
	public void playerQuitEvent(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		if(player.hasPermission("shift.staff"))
		{
			ShiftManager shiftManager = ShiftManager.getInstance();
			
			StaffMember staffMember = configManager.getStaffMembersConfig().getStaffMember(player);
			if(staffMember != null && shiftManager.onTheClock(staffMember))
			{
				shiftManager.clockOut(staffMember, configManager.getEventLabelConfig().getShiftEndDisconnected());
				
				staffMember.logEntry("Logged out");
			}
		}
	}
}