package com.antarescraft.kloudy.stafftimesheet.datamodels;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologui.HoloGUIPlugin;
import com.antarescraft.kloudy.hologui.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologui.playerguicomponents.PlayerGUIPageModel;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.managers.ShiftManager;

/**
 * Base Page Model that all StaffTimesheet page models inherit from. Contains staff member methods needed by all child models
 */

public abstract class BaseStaffTimesheetPageModel extends PlayerGUIPageModel
{
	protected StaffMember staffMember;

	public BaseStaffTimesheetPageModel(HoloGUIPlugin plugin, GUIPage guiPage, Player player, StaffMember staffMember)
	{
		super(plugin, guiPage, player);
		
		this.staffMember = staffMember;
	}
	
	public String staffMemberName()
	{
		return staffMember.getPlayerName();
	}
	
	public String staffMemberRank()
	{
		return staffMember.getRankTitle();
	}
	
	public String staffMemberTimeGoal()
	{
		return staffMember.getTimeGoalString();
	}
	
	public String staffMemberLoggedTime()
	{
		return staffMember.getLoggedTimeString();
	}
	
	public double staffMemberPercentTimeLogged()
	{
		return staffMember.getPercentageTimeCompleted();
	}
	
	public String staffMemberClockedIn()
	{
		if(ShiftManager.getInstance().onTheClock(staffMember)) return "Yes";

		return "No";
	}
}