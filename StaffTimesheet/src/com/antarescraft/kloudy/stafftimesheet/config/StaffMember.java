package com.antarescraft.kloudy.stafftimesheet.config;

import java.io.File;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import com.antarescraft.kloudy.hologuiapi.plugincore.config.*;

import com.antarescraft.kloudy.hologuiapi.plugincore.exceptions.*;
import com.antarescraft.kloudy.hologuiapi.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.plugincore.messaging.MessageManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;
import com.antarescraft.kloudy.stafftimesheet.util.IOManager;

/**
 * Represents a staff member
 * 
 * Contains all staff member data for a player from staff-members.yml
 * 
 * This class is instantiated populated by the ConfigParser library
 */
public class StaffMember
{
	private StaffMember(){}
	
	private Player player;
	
	@ConfigElementKey
	private String playerName;
	
	@ConfigProperty(key = "uuid")
	public String playerUUIDString;
	
	@OptionalConfigProperty
	@ConfigProperty(key = "clock-in-command")
	private String clockInCommand;
	
	@OptionalConfigProperty
	@ConfigProperty(key = "clock-out-command")
	private String clockOutCommand;
	
	@ConfigProperty(key = "rank-title")
	private String rankTitle;
	
	@StringConfigProperty(defaultValue = "00:00:00")
	@ConfigProperty(key = "time-goal")
	private String timeGoalString;
	
	@StringConfigProperty(defaultValue = "00:00:00")
	@ConfigProperty(key = "logged-time")
	private String loggedTimeString;
	
	@OptionalConfigProperty
	@BooleanConfigProperty(defaultValue = false)
	@ConfigProperty(key = "start-shift-on-login")
	private boolean startShiftOnLogin;
	
	@BooleanConfigProperty(defaultValue = false)
	@ConfigProperty(key = "super-admin")
	private boolean superAdmin;
	
	private void save()
	{
		File staffYaml = new File(String.format("plugins/%s/staff-members.yml", StaffTimesheet.pluginName));
		try 
		{
			ConfigParser.saveObject(StaffTimesheet.pluginName, staffYaml, "staff-members." + playerName, this);
		}
		catch (IOException e)
		{
			MessageManager.error(Bukkit.getConsoleSender(), String.format("An error occured while saving class %s to yml", this.getClass().getName()));
		}
	}
	
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
		
		save();
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

		save();
	}
	
	public void resetLoggedTime()
	{
		loggedTimeString = TimeFormat.getDurationFormatString(Duration.ZERO);
		
		save();
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
	
	public String getClockInCommand()
	{
		return clockInCommand;
	}
	
	public String getClockOutCommand()
	{
		return clockOutCommand;
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
		
		save();
	}
	
	public void setTimeGoal(Duration timeGoal)
	{
		timeGoalString = TimeFormat.getDurationFormatString(timeGoal);
		
		save();
	}
	
	public void setSuperAdmin(boolean superAdmin)
	{
		this.superAdmin = superAdmin;
		
		save();
	}
	
	public void setClockInCommand(String clockInCommand)
	{
		this.clockInCommand = clockInCommand;
		
		save();
	}
	
	public void setClockOutCommand(String clockOutCommand)
	{
		this.clockOutCommand = clockOutCommand;
		
		save();
	}
	
	public void setRankTitle(String rankTitle)
	{
		this.rankTitle = rankTitle;
		
		save();
	}
	
	public void setStartShiftOnLogin(boolean startShiftOnLogin)
	{
		this.startShiftOnLogin = startShiftOnLogin;
		
		save();
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