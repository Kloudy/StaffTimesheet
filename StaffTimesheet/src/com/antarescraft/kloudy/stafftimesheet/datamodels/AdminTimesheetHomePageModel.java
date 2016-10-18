package com.antarescraft.kloudy.stafftimesheet.datamodels;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologui.HoloGUIPlugin;
import com.antarescraft.kloudy.hologui.guicomponents.ButtonComponent;
import com.antarescraft.kloudy.hologui.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologui.guicomponents.ItemButtonComponent;
import com.antarescraft.kloudy.hologui.guicomponents.TextBoxComponent;
import com.antarescraft.kloudy.hologui.handlers.ClickHandler;
import com.antarescraft.kloudy.hologui.handlers.TextBoxUpdateHandler;
import com.antarescraft.kloudy.plugincore.messaging.MessageManager;
import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;

public class AdminTimesheetHomePageModel extends TimesheetHomePageModel
{
	private TextBoxComponent staffMemberSearchBox;
	private ButtonComponent manageStaffMemberBtn;
	
	public AdminTimesheetHomePageModel(final HoloGUIPlugin plugin, GUIPage guiPage, final Player player, final ConfigManager configManager) 
	{
		super(plugin, guiPage, player, configManager);
		
		logbookBtn = (ItemButtonComponent) guiPage.getComponent("logbook-btn");
		logbookBtn.registerClickHandler(new ClickHandler()
		{
			@Override
			public void onClick()
			{
				if(staffMember != null)
				{
					LogbookPageModel logbookModel = new LogbookPageModel(plugin, plugin.getGUIPages().get("timesheet-log"), player, staffMember);
					plugin.getHoloGUI().openGUIPage(plugin, player, "timesheet-log", logbookModel);
				}
				else
				{
					MessageManager.error(player, "Sorry, please select a staff member first");
				}
			}
		});
		
		staffMemberSearchBox = (TextBoxComponent) guiPage.getComponent("player-name-text-box");
		staffMemberSearchBox.registerTextBoxUpdateHandler(new TextBoxUpdateHandler()
		{
			@Override
			public void onUpdate(String value) 
			{
				staffMember = configManager.getStaffMember(value);//value may be null if the name searched isn't a staff member
			}
		});
		
		manageStaffMemberBtn = (ButtonComponent)guiPage.getComponent("manage-staff-member-btn");
		manageStaffMemberBtn.registerClickHandler(new ClickHandler()
		{
			@Override
			public void onClick()
			{
				if(staffMember != null)
				{
					ManageTimePageModel manageTimeModel = new ManageTimePageModel(plugin, plugin.getGUIPages().get("admin-manage-staff"), player, staffMember);
					plugin.getHoloGUI().openGUIPage(plugin, player, "admin-manage-staff", manageTimeModel);
				}
				else
				{
					MessageManager.error(player, "Sorry, please select a staff member first");
				}
			}
		});
	}
}