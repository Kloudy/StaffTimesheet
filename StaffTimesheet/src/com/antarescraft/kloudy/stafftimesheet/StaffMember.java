package com.antarescraft.kloudy.stafftimesheet;

import java.time.Duration;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;
import com.antarescraft.kloudy.stafftimesheet.util.IOManager;
import com.antarescraft.kloudy.stafftimesheet.util.TimeFormat;

public class StaffMember
{
	private Player player;
	private String playerName;
	private UUID playerUUID;
	private boolean superAdmin;
	private String clockInPermission;
	private String rankTitle;
	private Duration timeGoal;
	private Duration loggedTime;
	
	public StaffMember(String playerName, String playerUUID, boolean superAdmin, String clockInPermission, String timeGoal, 
			String rankTitle, String loggedTime)
	{
		this.playerName = playerName;
		this.playerUUID = UUID.fromString(playerUUID);
		this.superAdmin = superAdmin;
		this.clockInPermission = clockInPermission;
		this.rankTitle = rankTitle;
		this.timeGoal = TimeFormat.parseTimeFormat(timeGoal);
		this.loggedTime = TimeFormat.parseTimeFormat(loggedTime);
	}
	
	public void addLoggedTime(Duration time)
	{
		loggedTime = loggedTime.plus(time);
		
		String timeFormat = TimeFormat.getTimeFormat(loggedTime);
		ConfigManager.writePropertyToConfigFile("staff-members." + playerName + ".logged-time", timeFormat);
	}
	
	public void subtractLoggedTime(Duration time)
	{
		loggedTime = loggedTime.minus(time);
		
		String timeFormat = TimeFormat.getTimeFormat(loggedTime);
		ConfigManager.writePropertyToConfigFile("staff-members." + playerName + ".logged-time", timeFormat);
	}
	
	public void resetLoggedTime()
	{
		loggedTime = Duration.ZERO;
		
		String timeFormat = TimeFormat.getTimeFormat(loggedTime);
		ConfigManager.writePropertyToConfigFile("staff-members." + playerName + ".logged-time", timeFormat);
	}
	
	public void logEntry(String text)
	{
		String timestamp = TimeFormat.generateTimestamp("[hh:mm:ss]: ");
		String logEntryLine = timestamp + " " + text;
			
		IOManager.saveLogEntry(this, logEntryLine);
	}
	
	/*
	 * Getter functions
	 */
	
	public Player getPlayer()
	{
		if(player == null)
		{
			for(Player player : Bukkit.getOnlinePlayers())
			{
				if(playerUUID.equals(player.getUniqueId()))
				{
					this.player = player;
					break;
				}
			}
		}
		
		return player;
	}
	
	public String getPlayerName()
	{
		return playerName;
	}
	
	public UUID getUUID()
	{
		return playerUUID;
	}
	
	public boolean isSuperAdmin()
	{
		return superAdmin;
	}
	
	public String getClockInPermission()
	{
		return clockInPermission;
	}
	
	public String getRateTitle()
	{
		return rankTitle;
	}
	
	public String getTimeGoal()
	{
		return TimeFormat.getTimeFormat(timeGoal);
	}
	
	public String getLoggedTime()
	{
		return TimeFormat.getTimeFormat(loggedTime);
	}
}