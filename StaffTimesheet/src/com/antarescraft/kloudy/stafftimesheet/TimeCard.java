package com.antarescraft.kloudy.stafftimesheet;

/**
 * Represents a staff member's time card
 */
public class TimeCard
{
	private StaffMember staffMember;
	private long startTime;//milliseconds
	
	public TimeCard(StaffMember staffMember, long startTime)
	{
		this.staffMember = staffMember;
		this.startTime = startTime;
	}
	
	public StaffMember getStaffMember()
	{
		return staffMember;
	}
	
	public long getStartTime()
	{
		return startTime;
	}
}