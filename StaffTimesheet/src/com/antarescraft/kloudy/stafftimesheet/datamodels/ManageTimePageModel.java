package com.antarescraft.kloudy.stafftimesheet.datamodels;

import java.time.Duration;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologui.HoloGUIPlugin;
import com.antarescraft.kloudy.hologui.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologui.guicomponents.TextBoxComponent;
import com.antarescraft.kloudy.hologui.guicomponents.ToggleSwitchComponent;
import com.antarescraft.kloudy.hologui.guicomponents.ValueScrollerComponent;
import com.antarescraft.kloudy.hologui.handlers.ScrollHandler;
import com.antarescraft.kloudy.hologui.playerguicomponents.PlayerGUIPageModel;
import com.antarescraft.kloudy.hologui.scrollvalues.AbstractScrollValue;
import com.antarescraft.kloudy.hologui.scrollvalues.DurationScrollValue;
import com.antarescraft.kloudy.plugincore.messaging.MessageManager;
import com.antarescraft.kloudy.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;

import net.md_5.bungee.api.ChatColor;

public class ManageTimePageModel extends PlayerGUIPageModel
{
	private ValueScrollerComponent loggedTimeScroller;
	private ValueScrollerComponent loggedTimeHMSScroller;
	private ValueScrollerComponent timeGoalScroller;
	private ValueScrollerComponent timeGoalHMSScroller;
	private ToggleSwitchComponent superAdminToggle;
	private TextBoxComponent clockInPermissionTextBox;
	
	private StaffMember staffMember;

	public ManageTimePageModel(final HoloGUIPlugin plugin, final GUIPage guiPage, final Player player, final StaffMember staffMember) 
	{
		super(plugin, guiPage, player);
		
		this.staffMember = staffMember;
		
		loggedTimeScroller = (ValueScrollerComponent)guiPage.getComponent("logged-time-scroller");
		loggedTimeScroller.setValue(player, new DurationScrollValue(staffMember.getLoggedTime(), 
				TimeFormat.getMinDuration().plusHours(1), TimeFormat.getMinDuration(), staffMember.getTimeGoal()));
		
		timeGoalScroller = (ValueScrollerComponent)guiPage.getComponent("time-goal-scroller");
		timeGoalScroller.setValue(player, new DurationScrollValue(staffMember.getLoggedTime(), 
				TimeFormat.getMinDuration().plusHours(1), TimeFormat.getMinDuration(), TimeFormat.getMaxDuration()));
		
		superAdminToggle = (ToggleSwitchComponent)guiPage.getComponent("super-admin-toggle");
		superAdminToggle.setPlayerToggleSwitchState(player, staffMember.isSuperAdmin());
		
		clockInPermissionTextBox = (TextBoxComponent)guiPage.getComponent("clock-in-permission");
		clockInPermissionTextBox.setPlayerTextBoxValue(player, staffMember.getClockInPermission());
		
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
	
	public String getStaffMemberName()
	{
		return staffMember.getPlayerName();
	}
	
	public UUID getStaffMemberUUID()
	{
		return staffMember.getUUID();
	}
	
	/*
	 * Save changes made on staff member settings page
	 */
	public void save()
	{
		staffMember.setLoggedTime((Duration)loggedTimeScroller.getPlayerScrollValue(player).getValue());
		staffMember.setTimeGoal((Duration)timeGoalScroller.getPlayerScrollValue(player).getValue());
		staffMember.setSuperAdmin(superAdminToggle.getPlayerToggleSwitchState(player));
		
		MessageManager.info(player, ChatColor.GREEN + "Saved staff member settings!");
	}
}