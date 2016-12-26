package com.antarescraft.kloudy.stafftimesheet.datamodels;

import java.time.Duration;

import com.antarescraft.kloudy.hologuiapi.plugincore.messaging.MessageManager;
import com.antarescraft.kloudy.hologuiapi.plugincore.time.TimeFormat;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologuiapi.HoloGUIPlugin;
import com.antarescraft.kloudy.hologuiapi.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologuiapi.guicomponents.TextBoxComponent;
import com.antarescraft.kloudy.hologuiapi.guicomponents.ToggleSwitchComponent;
import com.antarescraft.kloudy.hologuiapi.guicomponents.ValueScrollerComponent;
import com.antarescraft.kloudy.hologuiapi.handlers.ScrollHandler;
import com.antarescraft.kloudy.hologuiapi.scrollvalues.AbstractScrollValue;
import com.antarescraft.kloudy.hologuiapi.scrollvalues.DurationScrollValue;
import com.antarescraft.kloudy.stafftimesheet.ShiftManager;
import com.antarescraft.kloudy.stafftimesheet.config.StaffMember;

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
	private TextBoxComponent clockInCommandTextBox;
	private TextBoxComponent clockOutCommandTextBox;
	private TextBoxComponent rankTitleTextBox;
	
	private StaffMember staffMember;

	public StaffMemberSettingsPageModel(final HoloGUIPlugin plugin, final GUIPage guiPage, final Player player, final StaffMember staffMember) 
	{
		super(plugin, guiPage, player, staffMember);
		
		this.staffMember = staffMember;
		
		loggedTimeScroller = (ValueScrollerComponent)guiPage.getComponent("logged-time-scroller");
		loggedTimeScroller.setPlayerScrollValue(player, new DurationScrollValue(staffMember.getLoggedTime(), 
				TimeFormat.getMinDuration().plusHours(1), TimeFormat.getMinDuration(), staffMember.getTimeGoal(), false));
		
		timeGoalScroller = (ValueScrollerComponent)guiPage.getComponent("time-goal-scroller");
		timeGoalScroller.setPlayerScrollValue(player, new DurationScrollValue(staffMember.getTimeGoal(), 
				TimeFormat.getMinDuration().plusHours(1), TimeFormat.getMinDuration(), TimeFormat.getMaxDuration(), false));
		
		superAdminToggle = (ToggleSwitchComponent)guiPage.getComponent("super-admin-toggle");
		superAdminToggle.setPlayerToggleSwitchState(player, staffMember.isSuperAdmin());
		
		startShiftOnLoginToggle = (ToggleSwitchComponent)guiPage.getComponent("start-shift-on-login-toggle");
		startShiftOnLoginToggle.setPlayerToggleSwitchState(player, staffMember.startShiftOnLogin());
		
		clockInCommandTextBox = (TextBoxComponent)guiPage.getComponent("clock-in-command");
		clockInCommandTextBox.setPlayerTextBoxValue(player, staffMember.getClockInCommand());
		
		clockOutCommandTextBox = (TextBoxComponent)guiPage.getComponent("clock-out-command");
		clockOutCommandTextBox.setPlayerTextBoxValue(player, staffMember.getClockInCommand());
		
		rankTitleTextBox = (TextBoxComponent)guiPage.getComponent("rank-title");
		rankTitleTextBox.setPlayerTextBoxValue(player, staffMember.getRankTitle());
		
		//register hours:minutes:second scroll handlers
		loggedTimeHMSScroller = (ValueScrollerComponent)guiPage.getComponent("logged-time-hms-scroller");
		loggedTimeHMSScroller.registerScrollHandler(player, new ScrollHandler()
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
		timeGoalHMSScroller.registerScrollHandler(player, new ScrollHandler()
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
		staffMember.setClockInCommand(clockInCommandTextBox.getPlayerTextBoxValue(player));
		staffMember.setClockOutCommand(clockOutCommandTextBox.getPlayerTextBoxValue(player));
		staffMember.setRankTitle(rankTitleTextBox.getPlayerTextBoxValue(player));
		staffMember.setStartShiftOnLogin(startShiftOnLoginToggle.getPlayerToggleSwitchState(player));
		
		//update the current billing period summary with the changes
		ShiftManager shiftManager = ShiftManager.getInstance();
		shiftManager.getCurrentBillingPeriod().updateStaffMemberSummary(staffMember);
		
		MessageManager.info(player, ChatColor.GREEN + "Saved staff member settings!");
	}
}