package com.antarescraft.kloudy.stafftimesheet.datamodels;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologui.HoloGUIPlugin;
import com.antarescraft.kloudy.hologui.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologui.guicomponents.ItemButtonComponent;
import com.antarescraft.kloudy.hologui.handlers.ClickHandler;
import com.antarescraft.kloudy.hologui.playerguicomponents.PlayerGUIPageModel;
import com.antarescraft.kloudy.stafftimesheet.ShiftManager;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;

public class TimesheetHomePageModel extends PlayerGUIPageModel
{
	protected ItemButtonComponent logbookBtn;
	protected StaffMember staffMember;
	protected ConfigManager configManager;
	
	public TimesheetHomePageModel(final HoloGUIPlugin plugin, GUIPage guiPage, final Player player, ConfigManager configManager) 
	{
		super(plugin, guiPage, player);
		
		staffMember = configManager.getStaffMember(player);
		
		logbookBtn = (ItemButtonComponent) guiPage.getComponent("logbook-btn");
		logbookBtn.registerClickHandler(new ClickHandler()
		{
			@Override
			public void onClick()
			{
				LogbookPageModel logbookModel = new LogbookPageModel(plugin, plugin.getGUIPages().get("admin-timesheet-log"), player, staffMember);
				plugin.getHoloGUI().openGUIPage(plugin, player, "timesheet-log", logbookModel);
			}
		});
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