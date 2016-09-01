package com.antarescraft.kloudy.stafftimesheet.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.antarescraft.kloudy.stafftimesheet.ShiftEndReason;
import com.antarescraft.kloudy.stafftimesheet.ShiftManager;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;

public class PlayerQuitEventListener implements Listener
{
	private ConfigManager configManager;
	
	public PlayerQuitEventListener(ConfigManager configManager)
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
			
			StaffMember staffMember = configManager.getStaffMember(player);
			if(staffMember != null && shiftManager.onTheClock(staffMember))
			{
				shiftManager.clockOut(staffMember, ShiftEndReason.PLAYER_QUIT);
				
				staffMember.logEntry("Logged out");
			}
		}
	}
}