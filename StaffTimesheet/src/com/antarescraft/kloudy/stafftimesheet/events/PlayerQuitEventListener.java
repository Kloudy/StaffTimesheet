package com.antarescraft.kloudy.stafftimesheet.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitEventListener implements Listener
{
	@EventHandler
	public void playerQuitEvent(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		if(player.hasPermission("shift.staff"))
		{
			
		}
	}
}