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
	private boolean superAdmin;
	
	public StaffMember(String playerName, String playerUUID, String clockInPermission, Duration timeGoal, 
			String rankTitle, Duration loggedTime, boolean startShiftOnLogin, boolean superAdmin)
	{
		this.playerName = playerName;
		this.playerUUID = UUID.fromString(playerUUID);
		this.clockInPermission = clockInPermission;
		this.rankTitle = rankTitle;
		this.startShiftOnLogin = startShiftOnLogin;
		this.timeGoal = timeGoal;
		this.loggedTime = loggedTime;
		this.superAdmin = superAdmin;
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
		ConfigManager.writePropertyToConfigFile("staff-members.yml", "staff-members." + playerName + ".logged-time", timeFormat);
	}
	
	public void subtractLoggedTime(Duration time) throws DurationUnderflowException
	{
		Duration diffDuration = loggedTime.minus(time);
		
		if(diffDuration.compareTo(TimeFormat.getMinDuration()) < 0)
		{
			throw new DurationUnderflowException();
		}
		
		loggedTime = loggedTime.minus(diffDuration);
		
		String durationFormat = TimeFormat.getDurationFormatString(loggedTime);
		ConfigManager.writePropertyToConfigFile("staff-members.yml", "staff-members." + playerName + ".logged-time", durationFormat);
	}
	
	public void resetLoggedTime()
	{
		loggedTime = Duration.ZERO;
		
		String durationFormat = TimeFormat.getDurationFormatString(loggedTime);
		ConfigManager.writePropertyToConfigFile("staff-members.yml", "staff-members." + playerName + ".logged-time", durationFormat);
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
	
	public String getTimeGoalString()
	{
		return TimeFormat.getDurationFormatString(timeGoal);
	}
	
	public Duration getTimeGoal()
	{
		return timeGoal;
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
	
	public boolean isSuperAdmin()
	{
		return superAdmin;
	}
	
	/*
	 * Setter Functions - These functions also update the staff member's config values in staff-members.yml
	 */
	
	public void setLoggedTime(Duration loggedTime)
	{
		this.loggedTime = loggedTime;
		
		String durationFormat = TimeFormat.getDurationFormatString(this.loggedTime);
		ConfigManager.writePropertyToConfigFile("staff-members.yml", "staff-members." + playerName + ".logged-time", durationFormat);		
	}
	
	public void setTimeGoal(Duration timeGoal)
	{
		this.timeGoal = timeGoal;
		
		String durationFormat = TimeFormat.getDurationFormatString(this.timeGoal);
		ConfigManager.writePropertyToConfigFile("staff-members.yml", "staff-members." + playerName + ".time-goal", durationFormat);
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
			if(staffMemberObj.getUUID().equals(this.playerUUID))
			{
				return true;
			}
		}
		
		return false;
	}
}