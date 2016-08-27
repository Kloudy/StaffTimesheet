package com.antarescraft.kloudy.stafftimesheet;

import org.bukkit.entity.Player;

public class TimeCard
{
	private Player player;
	private long startTime;//milliseconds
	
	public TimeCard(Player player, long startTime)
	{
		this.player = player;
		this.startTime = startTime;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public long getStartTime()
	{
		return startTime;
	}
}