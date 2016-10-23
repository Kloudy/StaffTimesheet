package com.antarescraft.kloudy.stafftimesheet.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.antarescraft.kloudy.stafftimesheet.ShiftManager;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;

public class PlayerJoinEventListener implements Listener
{
	private ConfigManager configManager;
	
	public PlayerJoinEventListener(ConfigManager configManager)
	{
		this.configManager = configManager;
	}
	
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		
		StaffMember staffMember = configManager.getStaffMember(player);
		if(staffMember != null && staffMember.startShiftOnLogin())
		{
			ShiftManager.getInstance().clockIn(staffMember, configManager.getShiftStartLabel());
			
			staffMember.getPlayer().sendMessage(configManager.getShiftStartMessage(staffMember));
		}
	}
}