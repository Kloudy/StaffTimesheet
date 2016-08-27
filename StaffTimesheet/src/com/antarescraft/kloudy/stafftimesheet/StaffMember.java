package com.antarescraft.kloudy.stafftimesheet;

import java.util.UUID;

import org.bukkit.entity.Player;

public class StaffMember
{
	private String playerName;
	private String playerUUID;
	private boolean superAdmin;
	private String clockInPermission;
	private String monthlyTimeGoal;
	private String rankTitle;
	private String hoursLoggedThisMonth;
	
	public StaffMember(String playerName, String playerUUID, boolean superAdmin, String clockInPermission, String monthlyTimeGoal, 
			String rankTitle, String hoursLoggedThisMonth)
	{
		this.playerName = playerName;
		this.playerUUID = playerUUID;
		this.superAdmin = superAdmin;
		this.clockInPermission = clockInPermission;
		this.monthlyTimeGoal = monthlyTimeGoal;
		this.rankTitle = rankTitle;
		this.hoursLoggedThisMonth = hoursLoggedThisMonth;
	}
	
	public String getPlayerName()
	{
		return playerName;
	}
	
	public UUID getPlayerUUID()
	{
		return UUID.fromString(playerUUID);
	}
	
	public boolean isSuperAdmin()
	{
		return superAdmin;
	}
	
	public String getClockInPermission()
	{
		return clockInPermission;
	}
}