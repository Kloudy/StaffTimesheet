package com.antarescraft.kloudy.stafftimesheet.datamodels;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologuiapi.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologuiapi.guicomponents.ItemButtonComponent;
import com.antarescraft.kloudy.hologuiapi.handlers.ClickHandler;
import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;
import com.antarescraft.kloudy.stafftimesheet.config.StaffTimesheetConfig;

/**
 * Represents a data model for the non-superadmin timesheet home page
 */
public class TimesheetHomePageModel extends BaseStaffTimesheetPageModel
{
	protected ItemButtonComponent logbookBtn;
	
	public TimesheetHomePageModel(final StaffTimesheet plugin, GUIPage guiPage, final Player player) 
	{
		super(plugin, guiPage, player, StaffTimesheetConfig.getConfig(plugin).getStaffMembersConfig().getStaffMember(player));
				
		logbookBtn = (ItemButtonComponent) guiPage.getComponent("logbook-btn");
		logbookBtn.registerClickHandler(player, new ClickHandler()
		{
			@Override
			public void onClick()
			{
				LogbookPageModel logbookModel = new LogbookPageModel(plugin, plugin.getGUIPages().get("timesheet-log"), player, staffMember);
				plugin.getHoloGUIApi().openGUIPage(plugin, player, "timesheet-log", logbookModel);
			}
		});
	}
}