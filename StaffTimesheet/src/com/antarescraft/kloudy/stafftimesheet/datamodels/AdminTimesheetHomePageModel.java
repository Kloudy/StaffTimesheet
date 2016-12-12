package com.antarescraft.kloudy.stafftimesheet.datamodels;

import java.util.ArrayList;
import java.util.Collection;

import com.antarescraft.kloudy.hologuiapi.plugincore.messaging.MessageManager;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologuiapi.HoloGUIPlugin;
import com.antarescraft.kloudy.hologuiapi.guicomponents.ButtonComponent;
import com.antarescraft.kloudy.hologuiapi.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologuiapi.guicomponents.ItemButtonComponent;
import com.antarescraft.kloudy.hologuiapi.guicomponents.ValueScrollerComponent;
import com.antarescraft.kloudy.hologuiapi.handlers.ClickHandler;
import com.antarescraft.kloudy.hologuiapi.handlers.ScrollHandler;
import com.antarescraft.kloudy.hologuiapi.scrollvalues.AbstractScrollValue;
import com.antarescraft.kloudy.hologuiapi.scrollvalues.ListScrollValue;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.config.StaffTimesheetConfig;

/**
 * Represents a data model for the superadmin timesheet home page
 */
public class AdminTimesheetHomePageModel extends TimesheetHomePageModel
{
	private ValueScrollerComponent staffMemberSelector;
	private ButtonComponent manageStaffMemberBtn;
	private ButtonComponent billingPeriodHistoryBtn;
	
	public AdminTimesheetHomePageModel(final HoloGUIPlugin plugin, GUIPage guiPage, final Player player, final StaffTimesheetConfig configManager) 
	{
		super(plugin, guiPage, player, configManager);
		
		logbookBtn = (ItemButtonComponent) guiPage.getComponent("logbook-btn");
		logbookBtn.registerClickHandler(player, new ClickHandler()
		{
			@Override
			public void onClick()
			{
				if(staffMember != null)
				{
					LogbookPageModel logbookModel = new LogbookPageModel(plugin, plugin.getGUIPages().get("timesheet-log"), player, staffMember);
					plugin.getHoloGUIApi().openGUIPage(plugin, player, "timesheet-log", logbookModel);
				}
				else
				{
					MessageManager.error(player, "Sorry, please select an existing staff member first");
				}
			}
		});
		
		billingPeriodHistoryBtn = (ButtonComponent)guiPage.getComponent("billing-period-history-btn");
		billingPeriodHistoryBtn.registerClickHandler(player, new ClickHandler()
		{
			@Override
			public void onClick()
			{
				if(staffMember != null)
				{
					BillingPeriodHistoryPageModel billingPeriodHistoryModel = new BillingPeriodHistoryPageModel(plugin, plugin.getGUIPages().get("billing-period-history"), player, staffMember, configManager);
					plugin.getHoloGUIApi().openGUIPage(plugin, player, "billing-period-history", billingPeriodHistoryModel);
				}
				else
				{
					MessageManager.error(player, "Sorry, please select an existing staff member first");
				}
			}
		});
		
		staffMemberSelector = (ValueScrollerComponent) guiPage.getComponent("staff-member-selector");
		
		Collection<StaffMember> staffMembers = (Collection<StaffMember>)configManager.getStaffMembersConfig().getAllStaffMembers();
		String[] staffMemberNames = new String[staffMembers.size()];
		int viewerIndex = 0;
		int i = 0;
		for(StaffMember staffMember : staffMembers)
		{
			if(staffMember.getUUID().equals(player.getUniqueId()))//find the view of this page in the staff members list and display them in the scroller first
			{
				viewerIndex = i;
			}
			
			staffMemberNames[i] = staffMember.getPlayerName();
			
			i++;
		}
		
		String temp = staffMemberNames[0];
		staffMemberNames[0] = staffMemberNames[viewerIndex];
		staffMemberNames[viewerIndex] = temp;
		
		ArrayList<String> names = new ArrayList<String>();
		for(int j = 0; j < staffMemberNames.length; j++)
		{
			names.add(staffMemberNames[j]);
		}
		
		staffMemberSelector.setPlayerScrollValue(player, new ListScrollValue(names));
		staffMemberSelector.registerScrollHandler(player, new ScrollHandler()
		{
			@Override
			public void onScroll(AbstractScrollValue<?, ?> value) 
			{
				ListScrollValue listValue = (ListScrollValue)value;
				System.out.println(listValue.toString());
				staffMember = configManager.getStaffMembersConfig().getStaffMember(listValue.toString());
			}
		});
		
		manageStaffMemberBtn = (ButtonComponent)guiPage.getComponent("staff-member-settings-btn");
		manageStaffMemberBtn.registerClickHandler(player, new ClickHandler()
		{
			@Override
			public void onClick()
			{
				if(staffMember != null)
				{
					StaffMemberSettingsPageModel manageTimeModel = new StaffMemberSettingsPageModel(plugin, plugin.getGUIPages().get("admin-manage-staff"), player, staffMember);
					plugin.getHoloGUIApi().openGUIPage(plugin, player, "admin-manage-staff", manageTimeModel);
				}
				else
				{
					MessageManager.error(player, "Sorry, please select a staff member first");
				}
			}
		});
	}
}