package com.antarescraft.kloudy.stafftimesheet;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.plugincore.exceptions.DurationOverflowException;
import com.antarescraft.kloudy.plugincore.exceptions.DurationUnderflowException;
import com.antarescraft.kloudy.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;
import com.antarescraft.kloudy.stafftimesheet.util.IOManager;

public class StaffMember
{
	private Player player;
	private String playerName;
	private UUID playerUUID;
	private String clockInPermission;
	private String rankTitle;
	private Duration timeGoal;
	private Duration loggedTime;
	private boolean startShiftOnLogin;
	
	public StaffMember(String playerName, String playerUUID, String clockInPermission, Duration timeGoal, 
			String rankTitle, Duration loggedTime, boolean startShiftOnLogin)
	{
		this.playerName = playerName;
		this.playerUUID = UUID.fromString(playerUUID);
		this.clockInPermission = clockInPermission;
		this.rankTitle = rankTitle;
		this.startShiftOnLogin = startShiftOnLogin;
		this.timeGoal = timeGoal;
		this.loggedTime = loggedTime;
	}
	
	public void addLoggedTime(Duration time) throws DurationOverflowException
	{
		Duration sumDuration = loggedTime.plus(time);

		if(sumDuration.compareTo(TimeFormat.getMaxDuration()) > 0)//sum results in duration larger than the max duration
		{
			throw new DurationOverflowException();
		}
		
		loggedTime = sumDuration;
		
		String timeFormat = TimeFormat.getDurationFormatString(loggedTime);
		ConfigManager.writePropertyToConfigFile("staff-members." + playerName + ".logged-time", timeFormat);
	}
	
	public void subtractLoggedTime(Duration time) throws DurationUnderflowException
	{
		Duration diffDuration = loggedTime.minus(time);
		
		if(diffDuration.compareTo(TimeFormat.getMinDuration()) < 0)
		{
			
		}
		
		loggedTime = loggedTime.minus(diffDuration);
		
		String durationFormat = TimeFormat.getDurationFormatString(loggedTime);
		ConfigManager.writePropertyToConfigFile("staff-members." + playerName + ".logged-time", durationFormat);
	}
	
	public void resetLoggedTime()
	{
		loggedTime = Duration.ZERO;
		
		String durationFormat = TimeFormat.getDurationFormatString(loggedTime);
		ConfigManager.writePropertyToConfigFile("staff-members." + playerName + ".logged-time", durationFormat);
	}
	
	public void setLoggedTime(Duration loggedTime)
	{
		this.loggedTime = loggedTime;
		String durationFormat = TimeFormat.getDurationFormatString(this.loggedTime);
		ConfigManager.writePropertyToConfigFile("staff-members." + playerName + ".logged-time", durationFormat);
	}
	
	public void logEntry(String text)
	{
		String timestamp = TimeFormat.generateTimestamp("[hh:mm:ss]: ");
		String logEntryLine = timestamp + text;
			
		IOManager.saveLogEntry(this, logEntryLine);
	}
	
	public ArrayList<String> getLogEntry(Calendar date)
	{
		return IOManager.getLogFile(this, date);
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
		double loggedTimeSeconds = (double)loggedTime.getSeconds();
		double timeGoalSeconds = (double)timeGoal.getSeconds();

		DecimalFormat df = new DecimalFormat("#.###");
		df.setRoundingMode(RoundingMode.HALF_UP);
		double percent = Double.parseDouble(df.format(loggedTimeSeconds / timeGoalSeconds));
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
		return TimeFormat.getDurationFormatString(timeGoal);
	}
	
	public Duration getLoggedTime()
	{
		return loggedTime;
	}
	
	public String getLoggedTimeString()
	{
		return TimeFormat.getDurationFormatString(loggedTime);
	}
	
	public boolean startShiftOnLogin()
	{
		return startShiftOnLogin;
	}
}