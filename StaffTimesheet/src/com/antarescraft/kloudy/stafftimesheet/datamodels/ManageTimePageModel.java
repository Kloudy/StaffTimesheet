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
	
	private StaffMember staffMember;

	public ManageTimePageModel(final HoloGUIPlugin plugin, GUIPage guiPage, Player player, final StaffMember staffMember) 
	{
		super(plugin, guiPage, player);
		
		this.staffMember = staffMember;
				
		guiPage.registerPageLoadHandler(new GUIPageLoadHandler()
		{
			@Override
			public void onPageLoad(PlayerGUIPage loadedPage)
			{
				playerGUIPage = loadedPage;
				
				//render staff member logged time scroller
				GUIComponentProperties properties = new GUIComponentProperties(plugin, "logged-time-scroller", "admin-manage-staff", 
						new ComponentPosition(0, 0.5), "&lLogged Time", 10, true, false);
				
				ClickableGUIComponentProperties clickableProperties = new ClickableGUIComponentProperties(null, false, parseSound("UI_BUTTON_CLICK"), 0.5f, 2, null, null);
				
				DurationComponentValue durationValue = new DurationComponentValue(staffMember.getLoggedTime(), TimeFormat.getMinDuration().plusHours(1), 
						TimeFormat.getMinDuration(), staffMember.getTimeGoal());
				
				loggedTimeScroller = new ValueScrollerComponent(properties, clickableProperties, 
						parseSound("BLOCK_LAVA_POP"), 0.1f, durationValue);
				
				playerGUIPage.renderComponent(loggedTimeScroller);
				
				//render staff member time goal scroller
				properties = new GUIComponentProperties(plugin, "time-goal-scroller", "admin-manage-staff", 
						new ComponentPosition(0, 0.2), "&lTime Goal", 10, true, false);
				
				clickableProperties = new ClickableGUIComponentProperties(null, false, parseSound("UI_BUTTON_CLICK"), 0.5f, 2, null, null);
				
				durationValue = new DurationComponentValue(staffMember.getTimeGoal(), TimeFormat.getMinDuration().plusHours(1), 
						TimeFormat.getMinDuration(), TimeFormat.getMaxDuration());
				
				timeGoalScroller = new ValueScrollerComponent(properties, clickableProperties, 
						parseSound("BLOCK_LAVA_POP"), 0.1f, durationValue);
				
				playerGUIPage.renderComponent(timeGoalScroller);
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
	 * Save changes made on staff member page
	 */
	public void save()
	{
		staffMember.setLoggedTime((Duration)loggedTimeScroller.getPlayerScrollValue(player).getValue());
		staffMember.setTimeGoal((Duration)timeGoalScroller.getPlayerScrollValue(player).getValue());
		
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