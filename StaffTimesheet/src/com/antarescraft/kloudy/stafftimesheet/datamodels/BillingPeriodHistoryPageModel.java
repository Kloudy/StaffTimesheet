package com.antarescraft.kloudy.stafftimesheet.datamodels;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologui.HoloGUIPlugin;
import com.antarescraft.kloudy.hologui.guicomponents.ButtonComponent;
import com.antarescraft.kloudy.hologui.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologui.handlers.ClickHandler;
import com.antarescraft.kloudy.hologui.handlers.GUIPageLoadHandler;
import com.antarescraft.kloudy.hologui.playerguicomponents.PlayerGUIPage;
import com.antarescraft.kloudy.hologui.plugincore.messaging.MessageManager;
import com.antarescraft.kloudy.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.stafftimesheet.BillingPeriod;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.StaffMemberSummary;
import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;

public class BillingPeriodHistoryPageModel extends BaseStaffTimesheetPageModel
{
	private ConfigManager configManager;
	
	private PlayerGUIPage playerGUIPage;
	
	private ArrayList<BillingPeriod> billingPeriodHistory;
	
	private ButtonComponent nextPageBtn;
	private ButtonComponent prevPageBtn;
	private ButtonComponent deleteBillingPeriodBtn;
	
	private int page;
	
	public BillingPeriodHistoryPageModel(HoloGUIPlugin plugin, GUIPage guiPage, Player player, StaffMember staffMember, final ConfigManager configManager)
	{
		super(plugin, guiPage, player, staffMember);
				
		this.configManager = configManager;
		
		billingPeriodHistory = ConfigManager.getAllBillingPeriods();//read all billing history data from 'staff-member-billing-period-history.yml'
			
		page = billingPeriodHistory.size()-1;
		
		nextPageBtn = (ButtonComponent)guiPage.getComponent("next-page-btn");
		prevPageBtn = (ButtonComponent)guiPage.getComponent("prev-page-btn");
		deleteBillingPeriodBtn = (ButtonComponent)guiPage.getComponent("delete-btn");
		
		nextPageBtn.registerClickHandler(player, new ClickHandler()
		{
			@Override
			public void onClick()
			{
				playerGUIPage.removeComponent("delete-btn");
				
				playerGUIPage.renderComponent(prevPageBtn);
				
				page++;
				
				if((page + 1) >= billingPeriodHistory.size())
				{
					playerGUIPage.removeComponent("next-page-btn");
				}
				
				BillingPeriod currentBillingPeriod = configManager.getCurrentBillingPeriod();
				BillingPeriod billingPeriod = billingPeriodHistory.get(page);
				
				if(!currentBillingPeriod.equals(billingPeriod))
				{
					playerGUIPage.renderComponent(deleteBillingPeriodBtn);
				}
			}
		});
		
		
		prevPageBtn.registerClickHandler(player, new ClickHandler()
		{
			@Override
			public void onClick()
			{
				playerGUIPage.removeComponent("delete-btn");
				
				playerGUIPage.renderComponent(nextPageBtn);
				
				page--;
				
				if(page <= 0)
				{
					playerGUIPage.removeComponent("prev-page-btn");
				}
				
				BillingPeriod currentBillingPeriod = configManager.getCurrentBillingPeriod();
				BillingPeriod billingPeriod = billingPeriodHistory.get(page);
				
				if(!currentBillingPeriod.equals(billingPeriod))
				{
					playerGUIPage.renderComponent(deleteBillingPeriodBtn);
				}
			}
		});
		
		guiPage.registerPageLoadHandler(new GUIPageLoadHandler()
		{
			@Override
			public void onPageLoad(PlayerGUIPage loadedPlayerGUIPage)
			{
				playerGUIPage = loadedPlayerGUIPage;
				
				if(billingPeriodHistory.size() > 1)
				{
					playerGUIPage.renderComponent(prevPageBtn);
				}
			}
		});
	}
	
	public String startDate()
	{
		BillingPeriod billingPeriod = getBillingPeriod();
		return TimeFormat.getDateFormat(billingPeriod.getStartDate());
	}
	
	public String endDate()
	{
		BillingPeriod billingPeriod = getBillingPeriod();
		return TimeFormat.getDateFormat(billingPeriod.getEndDate());
	}
	
	public String billingPeriodTimeGoal()
	{
		BillingPeriod billingPeriod = getBillingPeriod();
		StaffMemberSummary summary = billingPeriod.getStaffMemberSummary(staffMember);
		if(summary != null)
		{
			return summary.getTimeGoal();
		}
		
		return "00:00:00";
	}
	
	public String billingPeriodLoggedTime()
	{
		BillingPeriod billingPeriod = getBillingPeriod();
		StaffMemberSummary summary = billingPeriod.getStaffMemberSummary(staffMember);
		if(summary != null)
		{
			return summary.getLoggedTime();
		}
		
		return "00:00:00";
	}
	
	public String billingPeriodPercentTimeLogged()
	{
		BillingPeriod billingPeriod = getBillingPeriod();
		StaffMemberSummary summary = billingPeriod.getStaffMemberSummary(staffMember);
		if(summary != null)
		{
			return Double.toString(summary.getPercentTimeCompleted()) + "%";
		}
		
		return "0.0%";
	}
	
	public int currentPage()
	{
		return page + 1;
	}
	
	public int totalPages()
	{
		return billingPeriodHistory.size();
	}
	
	/*
	 * Get billing period for current page. Most recent billing periods are at the end of the list.
	 */
	private BillingPeriod getBillingPeriod()
	{
		return billingPeriodHistory.get(page);
	}
	
	public void deleteBillingHistory()
	{
		playerGUIPage.removeComponent("delete-btn");
		
		BillingPeriod billingPeriod = billingPeriodHistory.get(page);		
		billingPeriod.removeFromConfigFile();
		
		billingPeriodHistory.remove(page);
		
		playerGUIPage.removeComponent("next-page-btn");
		playerGUIPage.removeComponent("back-page-btn");
		
		page = 0;
		
		if(billingPeriodHistory.size() > 0)
		{
			playerGUIPage.renderComponent(deleteBillingPeriodBtn);
		}
	}
}