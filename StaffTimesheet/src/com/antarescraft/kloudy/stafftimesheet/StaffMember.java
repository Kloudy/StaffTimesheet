package com.antarescraft.kloudy.stafftimesheet;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.stafftimesheet.util.TimeFormat;

public class StaffMember
{
	private String playerName;
	private UUID playerUUID;
	private boolean superAdmin;
	private String clockInPermission;
	private TimeFormat monthlyTimeGoal;
	private String rankTitle;
	private TimeFormat hoursLoggedThisMonth;
	
	public StaffMember(String playerName, String playerUUID, boolean superAdmin, String clockInPermission, String monthlyTimeGoal, 
			String rankTitle, String hoursLoggedThisMonth)
	{
		this.playerName = playerName;
		this.playerUUID = UUID.fromString(playerUUID);
		this.superAdmin = superAdmin;
		this.clockInPermission = clockInPermission;
		this.monthlyTimeGoal = TimeFormat.parseTimeFormat(monthlyTimeGoal);
		this.rankTitle = rankTitle;
		this.hoursLoggedThisMonth = TimeFormat.parseTimeFormat(hoursLoggedThisMonth);
	}
	
	public String getPlayerName()
	{
		return playerName;
	}
	
	public UUID getPlayerUUID()
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
	
	public TimeFormat getMonthlyTimeGoal()
	{
		return monthlyTimeGoal;
	}
	
	public String getRateTitle()
	{
		return rankTitle;
	}
	
	public TimeFormat getHoursLoggedThisMonth()
	{
		return hoursLoggedThisMonth;
	}
	
	public void setHoursLoggedThisMonth(TimeFormat hoursLoggedThisMonth)
	{
		this.hoursLoggedThisMonth = hoursLoggedThisMonth;
		
	}
}