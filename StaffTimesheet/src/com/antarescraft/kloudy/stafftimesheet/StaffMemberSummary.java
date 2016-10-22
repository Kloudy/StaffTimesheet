package com.antarescraft.kloudy.stafftimesheet;

import java.time.Duration;
import java.util.UUID;

import com.antarescraft.kloudy.hologui.plugincore.time.TimeFormat;

/**
 * Represents a staff member's activity (time logged) during a particular billing period
 */

public class StaffMemberSummary
{
	private String staffMemberName;
	private UUID staffMemberUUID;
	private double percentTimeLogged;
	private Duration timeLogged;
	
	public StaffMemberSummary(StaffMember staffMember)
	{
		staffMemberName = staffMember.getPlayerName();
		staffMemberUUID = staffMember.getUUID();
	}
	
	public StaffMemberSummary(String staffMemberName, UUID staffMemberUUID, double percentTimeLogged, Duration timeLogged)
	{
		this.staffMemberName = staffMemberName;
		this.staffMemberUUID = staffMemberUUID;
		this.percentTimeLogged = percentTimeLogged;
		this.timeLogged = timeLogged;
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
	
	public double getPercentTimeLogged()
	{
		return percentTimeLogged;
	}
	
	public String getTimeLogged()
	{
		return TimeFormat.getDurationFormatString(timeLogged);
	}
	
	/*
	 * Setter Functions
	 */
	
	public void setPercentTimeLogged(double percentTimeLogged)
	{
		this.percentTimeLogged = percentTimeLogged;
	}
	
	public void setTimeLogged(Duration timeLogged)
	{
		this.timeLogged = timeLogged;
	}
}