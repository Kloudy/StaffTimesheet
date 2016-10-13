package com.antarescraft.kloudy.stafftimesheet.datamodels;

import java.util.ArrayList;
import java.util.Calendar;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologui.HoloGUIPlugin;
import com.antarescraft.kloudy.hologui.guicomponents.ButtonComponent;
import com.antarescraft.kloudy.hologui.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologui.guicomponents.ValueScrollerComponent;
import com.antarescraft.kloudy.hologui.handlers.ClickHandler;
import com.antarescraft.kloudy.hologui.playerguicomponents.PlayerGUIPageModel;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.util.IOManager;

/**
 * Represents a base data model for the logbook gui page
 */

public class LogbookPageModel extends PlayerGUIPageModel
{
	private ValueScrollerComponent dateScroller;
	private ButtonComponent nextBtn;
	private ButtonComponent backBtn;
	private StaffMember staffMember;
	private ArrayList<String> logLines;
	
	/**
	 * @param guiPage
	 * @param player 
	 * @param staffMember Staff member whose logs are being looked up
	 */
	public LogbookPageModel(HoloGUIPlugin plugin, GUIPage guiPage, Player player, StaffMember staffMember)
	{
		super(plugin, guiPage, player);
		
		this.staffMember = staffMember;
		
		dateScroller = (ValueScrollerComponent) guiPage.getComponent("date-value-scroller");
		nextBtn = (ButtonComponent)guiPage.getComponent("next-page-btn");
		backBtn = (ButtonComponent)guiPage.getComponent("prev-page-btn");
		
		logLines = getLogStrings();
		if(logLines == null) return;
		
		if(logLines.size() > 10)
		{
			
		}
		
		nextBtn.registerClickHandler(new ClickHandler()
		{
			@Override
			public void onClick()
			{
				
			}
		});
		
		backBtn.registerClickHandler(new ClickHandler()
		{
			@Override
			public void onClick()
			{
				
			}
		});
	}
	
	private ArrayList<String> getLogStrings()
	{
		Calendar date = (Calendar) dateScroller.getPlayerScrollValue(player).getValue();
		return IOManager.getLogFile(staffMember, date);
	}
}