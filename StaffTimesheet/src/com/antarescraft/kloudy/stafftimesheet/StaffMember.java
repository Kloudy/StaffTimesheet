package com.antarescraft.kloudy.stafftimesheet;

import java.math.RoundingMode;
import java.text.DecimalFormat;
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
	private boolean startShiftOnLogin;
	
	public StaffMember(String playerName, String playerUUID, boolean superAdmin, String clockInPermission, Duration timeGoal, 
			String rankTitle, Duration loggedTime, boolean startShiftOnLogin)
	{
		this.playerName = playerName;
		this.playerUUID = UUID.fromString(playerUUID);
		this.superAdmin = superAdmin;
		this.clockInPermission = clockInPermission;
		this.rankTitle = rankTitle;
		this.startShiftOnLogin = startShiftOnLogin;
		this.timeGoal = timeGoal;
		this.loggedTime = loggedTime;
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
	
	public double getPercentageTimeCompleted()
	{
		long loggedTimeSeconds = loggedTime.getSeconds();
		long timeGoalSeconds = timeGoal.getSeconds();
		int deltaTime = (int)(timeGoalSeconds - loggedTimeSeconds);
		double percentageCompleted = deltaTime / timeGoalSeconds;
		
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.HALF_UP);
		double percent = Double.parseDouble(df.format(percentageCompleted));
		if(percent > 1) percent = 1;
		
		return percent * 100;
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
	
	public String getRankTitle()
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
	
	public boolean startShiftOnLogin()
	{
		return startShiftOnLogin;
	}
}