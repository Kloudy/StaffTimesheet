package com.antarescraft.kloudy.stafftimesheet.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.antarescraft.kloudy.stafftimesheet.ShiftManager;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;

public class PlayerCommandPreprocessEventListener implements Listener
{
	private ConfigManager configManager;
	
	public PlayerCommandPreprocessEventListener(ConfigManager configManager)
	{
		this.configManager = configManager;
	}
	
	@EventHandler
	public void playerCommandPreprocessEvent(PlayerCommandPreprocessEvent event)
	{
		Player player = event.getPlayer();
		
		StaffMember staffMember = configManager.getStaffMember(player);
		if(staffMember != null && ShiftManager.getInstance().onTheClock(staffMember))
		{
			System.out.println("logging message");
			staffMember.logEntry(event.getMessage());
		}
	}
}