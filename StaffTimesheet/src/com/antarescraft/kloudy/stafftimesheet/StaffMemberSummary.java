package com.antarescraft.kloudy.stafftimesheet;

/**
 * Represents a staff member's activity during a particular billing period
 */

public class StaffMemberSummary
{
	private StaffMember staffMember;
	
	public StaffMemberSummary(StaffMember staffMember)
	{
		this.staffMember = staffMember;
	}
	
	public StaffMember getStaffMember()
	{
		return staffMember;
	}
}