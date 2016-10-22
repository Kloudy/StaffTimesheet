package com.antarescraft.kloudy.stafftimesheet;

import java.time.Duration;
import java.util.UUID;

import com.antarescraft.kloudy.hologui.plugincore.time.TimeFormat;

/**
 * Represents a staff member's activity (logged time & percentage of time goal logged) during a particular billing period
 */

public class StaffMemberSummary
{
	private String staffMemberName;
	private UUID staffMemberUUID;
	private double percentTimeCompleted;
	private Duration timeGoal;
	private Duration loggedTime;
	
	public StaffMemberSummary(StaffMember staffMember)
	{
		staffMemberName = staffMember.getPlayerName();
		staffMemberUUID = staffMember.getUUID();
		percentTimeCompleted = staffMember.getPercentageTimeCompleted();
		timeGoal = staffMember.getTimeGoal();
		loggedTime = staffMember.getLoggedTime();
	}
	
	public StaffMemberSummary(String staffMemberName, UUID staffMemberUUID, double percentTimeLogged, 
			Duration timeGoal, Duration timeLogged)
	{
		this.staffMemberName = staffMemberName;
		this.staffMemberUUID = staffMemberUUID;
		this.percentTimeCompleted = percentTimeLogged;
		this.timeGoal = timeGoal;
		this.loggedTime = timeLogged;
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
		return staffMemberUUID;
	}
	
	public double getPercentTimeCompleted()
	{
		return percentTimeCompleted;
	}
	
	public String getTimeGoal()
	{
		return TimeFormat.getDurationFormatString(timeGoal);
	}
	
	public String getLoggedTime()
	{
		return TimeFormat.getDurationFormatString(loggedTime);
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
		this.timeGoal = timeGoal;
	}
	
	public void setLoggedTime(Duration timeLogged)
	{
		this.loggedTime = timeLogged;
	}
}