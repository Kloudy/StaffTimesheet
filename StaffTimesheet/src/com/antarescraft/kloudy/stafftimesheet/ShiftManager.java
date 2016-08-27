package com.antarescraft.kloudy.stafftimesheet;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class ShiftManager
{
	private static ShiftManager instance;
	
	private HashMap<UUID, TimeCard> timeCards;//collection containing active staff
	
	public static ShiftManager getInstance()
	{
		if(instance == null)
		{
			instance = new ShiftManager();
		}
		
		return instance;
	}
	
	private ShiftManager()
	{
		timeCards = new HashMap<UUID, TimeCard>();
	}
	
	public boolean onTheClock(Player player)
	{
		return timeCards.containsKey(player.getUniqueId());
	}
	
	public boolean onTheClock(String playerName)
	{
		for(TimeCard timeCard : timeCards.values())
		{
			if(timeCard.getPlayer().getName().equals(playerName))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public TimeCard getTimeCard(Player player)
	{
		return timeCards.get(player.getUniqueId());
	}
	
	public TimeCard getTimeCard(String playerName)
	{
		for(TimeCard timeCard : timeCards.values())
		{
			if(timeCard.getPlayer().getName().equals(playerName))
			{
				return timeCard;
			}
		}
		
		return null;
	}
	
	public void startShift(Player player)
	{
		TimeCard timeCard = new TimeCard(player, System.currentTimeMillis());
		timeCards.put(player.getUniqueId(), timeCard);
	}
	
	public void endShift(Player player)
	{
		timeCards.remove(player.getUniqueId());
	}
	
	public void endShift(String playerName)
	{
		UUID playerUUID = null;
		
		for(TimeCard timeCard : timeCards.values())
		{
			if(timeCard.getPlayer().getName().equals(playerName))
			{
				playerUUID = timeCard.getPlayer().getUniqueId();
				break;
			}
		}
		
		if(playerUUID != null)
		{
			timeCards.remove(playerUUID);
		}
	}
	
	public void logEntry()
	{
		
	}
}