package com.antarescraft.kloudy.stafftimesheet;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import com.antarescraft.kloudy.hologuiapi.plugincore.config.annotations.BooleanConfigurationProperty;
import com.antarescraft.kloudy.hologuiapi.plugincore.config.annotations.ConfigurationElementKey;
import com.antarescraft.kloudy.hologuiapi.plugincore.config.annotations.StringConfigurationProperty;
import com.antarescraft.kloudy.hologuiapi.plugincore.exceptions.*;
import com.antarescraft.kloudy.hologuiapi.plugincore.time.TimeFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;
import com.antarescraft.kloudy.stafftimesheet.util.IOManager;

public class StaffMember
{
	private Player player;
	
	@ConfigurationElementKey
	private String playerName;
	
	@StringConfigurationProperty(key = "uuid")
	private String playerUUIDString;
	
	@StringConfigurationProperty(key = "clock-in-permission")
	private String clockInPermission;
	
	@StringConfigurationProperty(key = "rank-title")
	private String rankTitle;
	
	@StringConfigurationProperty(key = "time-goal", defaultValue = "00:00:00")
	private String timeGoalString;
	
	@StringConfigurationProperty(key = "logged-time", defaultValue = "00:00:00")
	private String loggedTimeString;
	
	@BooleanConfigurationProperty(key = "start-shift-on-login")
	private boolean startShiftOnLogin;
	
	@BooleanConfigurationProperty(key = "super-admin")
	private boolean superAdmin;
	
	public void addLoggedTime(Duration time) throws DurationOverflowException
	{
		Duration loggedTime = getLoggedTime();
		Duration sumDuration = loggedTime.plus(time);

		if(sumDuration.compareTo(TimeFormat.getMaxDuration()) > 0)//sum results in duration larger than the max duration
		{
			throw new DurationOverflowException();
		}
		
		loggedTime = sumDuration;
		loggedTimeString = TimeFormat.getDurationFormatString(loggedTime);
		
		ConfigManager.writePropertyToConfigFile("staff-members.yml", "staff-members." + playerName + ".logged-time", loggedTimeString);
	}
	
	public void subtractLoggedTime(Duration time) throws DurationUnderflowException
	{
		Duration loggedTime = getLoggedTime();
		Duration diffDuration = loggedTime.minus(time);
		
		if(diffDuration.compareTo(TimeFormat.getMinDuration()) < 0)
		{
			throw new DurationUnderflowException();
		}
		
		loggedTime = loggedTime.minus(diffDuration);
		
		loggedTimeString = TimeFormat.getDurationFormatString(loggedTime);
		
		ConfigManager.writePropertyToConfigFile("staff-members.yml", "staff-members." + playerName + ".logged-time", loggedTimeString);
	}
	
	public void resetLoggedTime()
	{
		loggedTimeString = TimeFormat.getDurationFormatString(Duration.ZERO);
		
		ConfigManager.writePropertyToConfigFile("staff-members.yml", "staff-members." + playerName + ".logged-time", loggedTimeString);
	}
	
	public void logEntry(String text)
	{
		if(text == null || (text != null && text.equals(""))) return;
		
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
				if(getUUID().equals(player.getUniqueId()))
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
		Duration loggedTime = getLoggedTime();
		Duration timeGoal = getTimeGoal();
		
		double loggedTimeSeconds = (double)loggedTime.getSeconds();
		double timeGoalSeconds = (double)timeGoal.getSeconds();

		DecimalFormat df = new DecimalFormat("#.#");
		df.setRoundingMode(RoundingMode.HALF_UP);
		double percent = Double.parseDouble(df.format((loggedTimeSeconds / timeGoalSeconds)*100));
		if(percent > 100) percent = 100;
		
		return percent;
	}
	
	public String getPlayerName()
	{
		return playerName;
	}
	
	public UUID getUUID()
	{
		return UUID.fromString(playerUUIDString);
	}
	
	public String getClockInPermission()
	{
		return clockInPermission;
	}
	
	public String getRankTitle()
	{
		return rankTitle;
	}
	
	public String getTimeGoalString()
	{
		return timeGoalString;
	}
	
	public Duration getTimeGoal()
	{
		Duration timeGoal = null;
		
		try
		{
			timeGoal = TimeFormat.parseDurationFormat(timeGoalString);
		} 
		catch (InvalidDurationFormatException e) {}
		
		return timeGoal;
	}
	
	public Duration getLoggedTime()
	{
		Duration loggedTime = null;
		
		try
		{
			loggedTime = TimeFormat.parseDurationFormat(loggedTimeString);
		} 
		catch (InvalidDurationFormatException e) {}
		
		return loggedTime;
	}
	
	public String getLoggedTimeString()
	{
		return loggedTimeString;
	}
	
	public boolean startShiftOnLogin()
	{
		return startShiftOnLogin;
	}
	
	public boolean isSuperAdmin()
	{
		return superAdmin;
	}
	
	/*
	 * Setter Functions - These functions also update the staff member's config values in staff-members.yml
	 */
	
	public void setLoggedTime(Duration loggedTime)
	{
		loggedTimeString = TimeFormat.getDurationFormatString(loggedTime);
		
		ConfigManager.writePropertyToConfigFile("staff-members.yml", "staff-members." + playerName + ".logged-time", loggedTimeString);		
	}
	
	public void setTimeGoal(Duration timeGoal)
	{
		timeGoalString = TimeFormat.getDurationFormatString(timeGoal);
		
		ConfigManager.writePropertyToConfigFile("staff-members.yml", "staff-members." + playerName + ".time-goal", timeGoalString);
	}
	
	public void setSuperAdmin(boolean superAdmin)
	{
		this.superAdmin = superAdmin;
		
		ConfigManager.writePropertyToConfigFile("staff-members.yml", "staff-members." + playerName + ".super-admin", this.superAdmin);
	}
	
	public void setClockInPermission(String clockInPermission)
	{
		this.clockInPermission = clockInPermission;
		
		ConfigManager.writePropertyToConfigFile("staff-members.yml", "staff-members." + playerName + ".clock-in-permission", this.clockInPermission);
	}
	
	public void setRankTitle(String rankTitle)
	{
		this.rankTitle = rankTitle;
		
		ConfigManager.writePropertyToConfigFile("staff-members.yml", "staff-members." + playerName + ".rank-title", this.rankTitle);
	}
	
	public void setStartShiftOnLogin(boolean startShiftOnLogin)
	{
		this.startShiftOnLogin = startShiftOnLogin;
		
		ConfigManager.writePropertyToConfigFile("staff-members.yml", "staff-members." + playerName + ".start-shift-on-login", this.startShiftOnLogin);
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof StaffMember)
		{
			StaffMember staffMemberObj = (StaffMember)obj;
			if(staffMemberObj.getUUID().equals(getUUID()))
			{
				return true;
			}
		}
		
		return false;
	}
}