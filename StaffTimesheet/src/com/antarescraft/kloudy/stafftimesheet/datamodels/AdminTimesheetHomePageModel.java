package com.antarescraft.kloudy.stafftimesheet.datamodels;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologui.HoloGUIPlugin;
import com.antarescraft.kloudy.hologui.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologui.guicomponents.TextBoxComponent;
import com.antarescraft.kloudy.hologui.handlers.TextBoxUpdateHandler;
import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;

public class AdminTimesheetHomePageModel extends TimesheetHomePageModel
{
	private TextBoxComponent staffMemberSearchBox;
	
	public AdminTimesheetHomePageModel(HoloGUIPlugin plugin, GUIPage guiPage, Player player, final ConfigManager configManager) 
	{
		super(plugin, guiPage, player, configManager);
		
		staffMemberSearchBox = (TextBoxComponent) guiPage.getComponent("player-name-text-box");
		staffMemberSearchBox.registerTextBoxUpdateHandler(new TextBoxUpdateHandler()
		{
			@Override
			public void onUpdate(String value) 
			{
				staffMember = configManager.getStaffMember(value);//value may be null if the name searched isn't a staff member
				System.out.println(value);
			}
		});
	}
	
	public String testABitch(String literal, Player player, String playerName)
	{
		return "say something I'm giving up on you";
	}
}