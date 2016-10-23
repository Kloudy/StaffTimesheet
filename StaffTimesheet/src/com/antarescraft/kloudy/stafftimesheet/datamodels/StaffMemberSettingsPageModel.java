package com.antarescraft.kloudy.stafftimesheet.datamodels;

import java.time.Duration;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologui.HoloGUIPlugin;
import com.antarescraft.kloudy.hologui.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologui.guicomponents.TextBoxComponent;
import com.antarescraft.kloudy.hologui.guicomponents.ToggleSwitchComponent;
import com.antarescraft.kloudy.hologui.guicomponents.ValueScrollerComponent;
import com.antarescraft.kloudy.hologui.handlers.ScrollHandler;
import com.antarescraft.kloudy.hologui.scrollvalues.AbstractScrollValue;
import com.antarescraft.kloudy.hologui.scrollvalues.DurationScrollValue;
import com.antarescraft.kloudy.plugincore.messaging.MessageManager;
import com.antarescraft.kloudy.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.stafftimesheet.ShiftManager;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;

import net.md_5.bungee.api.ChatColor;

/**
 * Represents a data model for the staff member settings page
 */
public class StaffMemberSettingsPageModel extends BaseStaffTimesheetPageModel
{
	private ValueScrollerComponent loggedTimeScroller;
	private ValueScrollerComponent loggedTimeHMSScroller;
	private ValueScrollerComponent timeGoalScroller;
	private ValueScrollerComponent timeGoalHMSScroller;
	private ToggleSwitchComponent superAdminToggle;
	private ToggleSwitchComponent startShiftOnLoginToggle;
	private TextBoxComponent clockInPermissionTextBox;
	private TextBoxComponent rankTitleTextBox;
	
	private StaffMember staffMember;

	public StaffMemberSettingsPageModel(final HoloGUIPlugin plugin, final GUIPage guiPage, final Player player, final StaffMember staffMember) 
	{
		super(plugin, guiPage, player, staffMember);
		
		this.staffMember = staffMember;
		
		loggedTimeScroller = (ValueScrollerComponent)guiPage.getComponent("logged-time-scroller");
		loggedTimeScroller.setPlayerScrollValue(player, new DurationScrollValue(staffMember.getLoggedTime(), 
				TimeFormat.getMinDuration().plusHours(1), TimeFormat.getMinDuration(), staffMember.getTimeGoal()));
		
		timeGoalScroller = (ValueScrollerComponent)guiPage.getComponent("time-goal-scroller");
		timeGoalScroller.setPlayerScrollValue(player, new DurationScrollValue(staffMember.getTimeGoal(), 
				TimeFormat.getMinDuration().plusHours(1), TimeFormat.getMinDuration(), TimeFormat.getMaxDuration()));
		
		superAdminToggle = (ToggleSwitchComponent)guiPage.getComponent("super-admin-toggle");
		superAdminToggle.setPlayerToggleSwitchState(player, staffMember.isSuperAdmin());
		
		startShiftOnLoginToggle = (ToggleSwitchComponent)guiPage.getComponent("start-shift-on-login-toggle");
		startShiftOnLoginToggle.setPlayerToggleSwitchState(player, staffMember.startShiftOnLogin());
		
		clockInPermissionTextBox = (TextBoxComponent)guiPage.getComponent("clock-in-permission");
		clockInPermissionTextBox.setPlayerTextBoxValue(player, staffMember.getClockInPermission());
		
		rankTitleTextBox = (TextBoxComponent)guiPage.getComponent("rank-title");
		rankTitleTextBox.setPlayerTextBoxValue(player, staffMember.getRankTitle());
		
		//register hours:minutes:second scroll handlers
		loggedTimeHMSScroller = (ValueScrollerComponent)guiPage.getComponent("logged-time-hms-scroller");
		loggedTimeHMSScroller.registerScrollHandler(new ScrollHandler()
		{
			@Override
			public void onScroll(AbstractScrollValue<?, ?> value)
			{
				DurationScrollValue loggedTimeValue = (DurationScrollValue)loggedTimeScroller.getPlayerScrollValue(player);
				
				if(value.toString().equals("Hours"))
				{
					loggedTimeValue.setStep(TimeFormat.getMinDuration().plusHours(1));
				}
				else if(value.toString().equals("Minutes"))
				{
					loggedTimeValue.setStep(TimeFormat.getMinDuration().plusMinutes(1));
				}
				else if(value.toString().equals("Seconds"))
				{
					loggedTimeValue.setStep(TimeFormat.getMinDuration().plusSeconds(1));
				}
			}
		});
		
		timeGoalHMSScroller = (ValueScrollerComponent)guiPage.getComponent("time-goal-hms-scroller");
		timeGoalHMSScroller.registerScrollHandler(new ScrollHandler()
		{
			@Override
			public void onScroll(AbstractScrollValue<?, ?> value)
			{
				DurationScrollValue timeGoalValue = (DurationScrollValue)timeGoalScroller.getPlayerScrollValue(player);
				
				if(value.toString().equals("Hours"))
				{
					timeGoalValue.setStep(TimeFormat.getMinDuration().plusHours(1));
				}
				else if(value.toString().equals("Minutes"))
				{
					timeGoalValue.setStep(TimeFormat.getMinDuration().plusMinutes(1));
				}
				else if(value.toString().equals("Seconds"))
				{
					timeGoalValue.setStep(TimeFormat.getMinDuration().plusSeconds(1));
				}
			}
		});
	}

	/*
	 * Save changes made on staff member settings page
	 */
	public void save()
	{
		staffMember.setLoggedTime((Duration)loggedTimeScroller.getPlayerScrollValue(player).getValue());
		staffMember.setTimeGoal((Duration)timeGoalScroller.getPlayerScrollValue(player).getValue());
		staffMember.setSuperAdmin(superAdminToggle.getPlayerToggleSwitchState(player));
		staffMember.setClockInPermission(clockInPermissionTextBox.getPlayerTextBoxValue(player));
		staffMember.setRankTitle(rankTitleTextBox.getPlayerTextBoxValue(player));
		staffMember.setStartShiftOnLogin(startShiftOnLoginToggle.getPlayerToggleSwitchState(player));
		
		//update the current billing period summary with the changes
		ShiftManager shiftManager = ShiftManager.getInstance();
		shiftManager.getCurrentBillingPeriod().updateStaffMemberSummary(staffMember);
		
		MessageManager.info(player, ChatColor.GREEN + "Saved staff member settings!");
	}
}