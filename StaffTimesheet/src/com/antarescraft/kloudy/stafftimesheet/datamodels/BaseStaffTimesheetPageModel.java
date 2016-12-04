package com.antarescraft.kloudy.stafftimesheet.datamodels;

import java.util.UUID;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologuiapi.HoloGUIPlugin;
import com.antarescraft.kloudy.hologuiapi.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologuiapi.playerguicomponents.PlayerGUIPageModel;
import com.antarescraft.kloudy.stafftimesheet.ShiftManager;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;

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
	
	public UUID staffMemberUUID()
	{
		return staffMember.getUUID();
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
	
	public String staffMemberStartShiftOnLogin()
	{
		if(staffMember.startShiftOnLogin()) return "Yes";
		
		return "No";
	}

	public void back()
	{
		plugin.getHoloGUIApi().displayPreviousGUIPage(player);
	}

	public void close()
	{
		plugin.getHoloGUIApi().closeGUIPage(player);
	}
}