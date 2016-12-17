package com.antarescraft.kloudy.stafftimesheet.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.antarescraft.kloudy.stafftimesheet.ShiftManager;
import com.antarescraft.kloudy.stafftimesheet.config.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.config.StaffMembersConfig;

public class PlayerCommandPreprocessEventListener implements Listener
{
	private StaffMembersConfig staffConfig;
	
	public PlayerCommandPreprocessEventListener(StaffMembersConfig staffConfig)
	{
		this.staffConfig = staffConfig;
	}
	
	@EventHandler
	public void playerCommandPreprocessEvent(PlayerCommandPreprocessEvent event)
	{
		Player player = event.getPlayer();
		
		StaffMember staffMember = staffConfig.getStaffMember(player);
		if(staffMember != null && ShiftManager.getInstance().onTheClock(staffMember))
		{
			staffMember.logEntry(event.getMessage());
		}
	}
}