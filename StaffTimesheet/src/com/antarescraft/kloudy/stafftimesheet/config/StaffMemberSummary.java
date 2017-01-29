package com.antarescraft.kloudy.stafftimesheet.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

import com.antarescraft.kloudy.hologuiapi.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.hologuiapi.plugincore.config.*;
import com.antarescraft.kloudy.hologuiapi.plugincore.config.annotations.*;

/**
 * Represents a staff member's activity (logged time & percentage of time goal logged) during a particular billing period
 *
 * This class is instantiated populated by the ConfigParser library
 */

public class StaffMemberSummary implements ConfigObject
{
	@ConfigElementKey
	private String playerUUIDString;
	
	@ConfigProperty(key = "player-name")
	private String staffMemberName;
	
	@ConfigProperty(key = "percent-time-logged")
	private double percentTimeCompleted;
	
	@ConfigProperty(key = "time-goal")
	private String timeGoal;
	
	@ConfigProperty(key = "logged-time")
	private String loggedTime;
	
	public StaffMemberSummary(){}
	
	public StaffMemberSummary(StaffMember staffMember)
	{
		staffMemberName = staffMember.getPlayerName();
		playerUUIDString = staffMember.getUUID().toString();
		percentTimeCompleted = staffMember.getPercentageTimeCompleted();
		timeGoal = TimeFormat.getDurationFormatString(staffMember.getTimeGoal());
		loggedTime = TimeFormat.getDurationFormatString(staffMember.getLoggedTime());
	}
	
	/*
	 * Getter functions
	 */
	
	public String getStaffMemberName()
	{
		return staffMemberName;
	}
	
	public UUID getStaffMemberUUID()
	{
		return UUID.fromString(playerUUIDString);
	}
	
	public double getPercentTimeCompleted()
	{
		return percentTimeCompleted;
	}
	
	public String getTimeGoal()
	{
		return timeGoal;
	}
	
	public String getLoggedTime()
	{
		return loggedTime;
	}
	
	/*
	 * Setter Functions
	 */
	
	public void setPercentTimeCompleted(double percentTimeLogged)
	{
		this.percentTimeCompleted = percentTimeLogged;
	}
	
	public void setTimeGoal(Duration timeGoal)
	{
		this.timeGoal = TimeFormat.getDurationFormatString(timeGoal);
	}
	
	public void setLoggedTime(Duration timeLogged)
	{
		this.loggedTime = TimeFormat.getDurationFormatString(timeLogged);
	}

	@Override
	public void configParseComplete(HashMap<String, Object> passthrougParams) {}
}