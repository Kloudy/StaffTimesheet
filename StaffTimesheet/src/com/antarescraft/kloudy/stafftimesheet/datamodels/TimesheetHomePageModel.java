package com.antarescraft.kloudy.stafftimesheet.datamodels;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologui.HoloGUIPlugin;
import com.antarescraft.kloudy.hologui.guicomponents.ButtonComponent;
import com.antarescraft.kloudy.hologui.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologui.guicomponents.ItemButtonComponent;
import com.antarescraft.kloudy.hologui.handlers.ClickHandler;
import com.antarescraft.kloudy.hologui.playerguicomponents.PlayerGUIPageModel;
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
				plugin.getHoloGUI().openGUIPage(plugin, player, "timesheet-log");
			}
		});
	}
}