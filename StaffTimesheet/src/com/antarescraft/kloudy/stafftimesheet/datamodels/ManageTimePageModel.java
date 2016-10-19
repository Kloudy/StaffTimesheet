package com.antarescraft.kloudy.stafftimesheet.datamodels;

import java.time.Duration;
import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologui.HoloGUIPlugin;
import com.antarescraft.kloudy.hologui.guicomponents.ClickableGUIComponentProperties;
import com.antarescraft.kloudy.hologui.guicomponents.ComponentPosition;
import com.antarescraft.kloudy.hologui.guicomponents.DurationComponentValue;
import com.antarescraft.kloudy.hologui.guicomponents.GUIComponentProperties;
import com.antarescraft.kloudy.hologui.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologui.guicomponents.TextBoxComponent;
import com.antarescraft.kloudy.hologui.guicomponents.ToggleSwitchComponent;
import com.antarescraft.kloudy.hologui.guicomponents.ValueScrollerComponent;
import com.antarescraft.kloudy.hologui.handlers.GUIPageLoadHandler;
import com.antarescraft.kloudy.hologui.playerguicomponents.PlayerGUIPage;
import com.antarescraft.kloudy.hologui.playerguicomponents.PlayerGUIPageModel;
import com.antarescraft.kloudy.plugincore.messaging.MessageManager;
import com.antarescraft.kloudy.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;

import net.md_5.bungee.api.ChatColor;

public class ManageTimePageModel extends PlayerGUIPageModel
{
	private PlayerGUIPage playerGUIPage;
	
	private ValueScrollerComponent loggedTimeScroller;
	private ValueScrollerComponent timeGoalScroller;
	private ToggleSwitchComponent superAdminToggle;
	private TextBoxComponent clockInPermissionTextBox;
	
	private StaffMember staffMember;

	public ManageTimePageModel(final HoloGUIPlugin plugin, final GUIPage guiPage, final Player player, final StaffMember staffMember) 
	{
		super(plugin, guiPage, player);
		
		this.staffMember = staffMember;
				
		guiPage.registerPageLoadHandler(new GUIPageLoadHandler()
		{
			@Override
			public void onPageLoad(PlayerGUIPage loadedPage)
			{
				playerGUIPage = loadedPage;
				
				loggedTimeScroller = (ValueScrollerComponent)guiPage.getComponent("logged-time-scroller");
				loggedTimeScroller.setValue(player, new DurationComponentValue(staffMember.getLoggedTime(), 
						TimeFormat.getMinDuration().plusHours(1), TimeFormat.getMinDuration(), staffMember.getTimeGoal()));
				
				timeGoalScroller = (ValueScrollerComponent)guiPage.getComponent("time-goal-scroller");
				timeGoalScroller.setValue(player, new DurationComponentValue(staffMember.getLoggedTime(), 
						TimeFormat.getMinDuration().plusHours(1), TimeFormat.getMinDuration(), TimeFormat.getMaxDuration()));
				
				
				superAdminToggle = (ToggleSwitchComponent)guiPage.getComponent("super-admin-toggle");
				superAdminToggle.setPlayerToggleSwitchState(player, staffMember.isSuperAdmin());
				
				clockInPermissionTextBox = (TextBoxComponent)guiPage.getComponent("clock-in-permission");
				clockInPermissionTextBox.setPlayerTextBoxValue(player, staffMember.getClockInPermission());
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
	
	private Sound parseSound(String soundStr)
	{
		Sound sound = null;
		try
		{
			sound = Sound.valueOf(soundStr);
		}
		catch(Exception e){}
		
		return sound;
	}
}