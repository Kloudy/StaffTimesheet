package com.antarescraft.kloudy.stafftimesheet.datamodels;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologui.HoloGUIPlugin;
import com.antarescraft.kloudy.hologui.guicomponents.AbstractIncrementableValue;
import com.antarescraft.kloudy.hologui.guicomponents.ButtonComponent;
import com.antarescraft.kloudy.hologui.guicomponents.ComponentPosition;
import com.antarescraft.kloudy.hologui.guicomponents.GUIComponentProperties;
import com.antarescraft.kloudy.hologui.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologui.guicomponents.LabelComponent;
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
	private ButtonComponent logBtn;
	private ButtonComponent nextBtn;
	private ButtonComponent backBtn;
	private int page;
	
	private StaffMember staffMember;
	private ArrayList<String> logLines;
	
	/**
	 * @param guiPage
	 * @param player 
	 * @param staffMember Staff member whose logs are being looked up
	 */
	public LogbookPageModel(final HoloGUIPlugin plugin, final GUIPage guiPage, final Player player, final StaffMember staffMember)
	{
		super(plugin, guiPage, player);
		
		this.staffMember = staffMember;
		
		dateScroller = (ValueScrollerComponent) guiPage.getComponent("date-value-scroller");
		logBtn = (ButtonComponent)guiPage.getComponent("log-btn");
		nextBtn = (ButtonComponent)guiPage.getComponent("next-page-btn");
		backBtn = (ButtonComponent)guiPage.getComponent("prev-page-btn");
		
		page = 0;
		
		logLines = getLogStrings();
		if(logLines == null) return;
		
		if(logLines.size() > 10)
		{
			
		}
		
		logBtn.registerClickHandler(new ClickHandler()
		{
			@Override
			public void onClick()
			{
				Calendar date = (Calendar)dateScroller.getPlayerScrollValue(player).getValue();
				ArrayList<String> logs = IOManager.getLogFile(staffMember, date);
				
				GUIComponentProperties properties = new GUIComponentProperties(plugin, "log-label-" + page, guiPage.getId(), new ComponentPosition(0, 0.04),
						null, 10, false, false);
				
				String[] logLines = new String[logs.size()];
				LabelComponent logLabel = new LabelComponent(properties, logLines);
				guiPage.addComponent(logLabel);
			}
		});
		
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