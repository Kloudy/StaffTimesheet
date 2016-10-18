package com.antarescraft.kloudy.stafftimesheet.datamodels;

import java.time.Duration;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologui.HoloGUIPlugin;
import com.antarescraft.kloudy.hologui.guicomponents.AbstractIncrementableValue;
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
	
	private ValueScrollerComponent timeScroller;
	
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
				
				//render staff member time scroller
				GUIComponentProperties properties = new GUIComponentProperties(plugin, "time-scroller", "admin-manage-staff", 
						new ComponentPosition(0.28, -0.52), "&lEdit Staff Member Logged Time", 10, true, false);
				
				ClickableGUIComponentProperties clickableProperties = new ClickableGUIComponentProperties(null, false, parseSound("UI_BUTTON_CLICK"), 0.5f, 2, null, null);
				
				DurationComponentValue durationValue = new DurationComponentValue(staffMember.getLoggedTime(), TimeFormat.getMinDuration().plusHours(1), 
						TimeFormat.getMinDuration(), staffMember.getTimeGoal());
				
				timeScroller = new ValueScrollerComponent(properties, clickableProperties, 
						parseSound("BLOCK_LAVA_POP"), 0.1f, durationValue);
				
				playerGUIPage.renderComponent(timeScroller);
			}
		});
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
	
	public void save()
	{
		staffMember.setLoggedTime((Duration)timeScroller.getPlayerScrollValue(player).getValue());
	}
}