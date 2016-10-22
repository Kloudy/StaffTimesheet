package com.antarescraft.kloudy.stafftimesheet.datamodels;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologui.HoloGUIPlugin;
import com.antarescraft.kloudy.hologui.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologui.guicomponents.ItemButtonComponent;
import com.antarescraft.kloudy.hologui.handlers.ClickHandler;
import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;

/**
 * Represents a data model for the non-superadmin timesheet home page
 */
public class TimesheetHomePageModel extends BaseStaffTimesheetPageModel
{
	protected ItemButtonComponent logbookBtn;
	protected ConfigManager configManager;
	
	public TimesheetHomePageModel(final HoloGUIPlugin plugin, GUIPage guiPage, final Player player, ConfigManager configManager) 
	{
		super(plugin, guiPage, player, configManager.getStaffMember(player));
				
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
}