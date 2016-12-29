package com.antarescraft.kloudy.stafftimesheet.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.antarescraft.kloudy.stafftimesheet.ShiftManager;
import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;
import com.antarescraft.kloudy.stafftimesheet.config.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.config.StaffTimesheetConfig;

public class PlayerQuitEventListener implements Listener
{
	private StaffTimesheet staffTimesheet;
	
	public PlayerQuitEventListener(StaffTimesheet staffTimesheet)
	{
		this.staffTimesheet = staffTimesheet;
	}
	
	@EventHandler
	public void playerQuitEvent(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		if(player.hasPermission("shift.staff"))
		{
			ShiftManager shiftManager = ShiftManager.getInstance();
			
			StaffTimesheetConfig config = StaffTimesheetConfig.getConfig(staffTimesheet);
			
			StaffMember staffMember = config.getStaffMembersConfig().getStaffMember(player);
			if(staffMember != null && shiftManager.onTheClock(staffMember))
			{
				shiftManager.clockOut(staffMember, config.getEventLabelConfig().getShiftEndDisconnected());
				
				staffMember.logEntry("Logged out");
			}
		}
	}
}