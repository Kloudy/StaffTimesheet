package com.antarescraft.kloudy.stafftimesheet.datamodels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologui.HoloGUIPlugin;
import com.antarescraft.kloudy.hologui.PlayerData;
import com.antarescraft.kloudy.hologui.guicomponents.ButtonComponent;
import com.antarescraft.kloudy.hologui.guicomponents.ComponentPosition;
import com.antarescraft.kloudy.hologui.guicomponents.GUIComponentProperties;
import com.antarescraft.kloudy.hologui.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologui.guicomponents.LabelComponent;
import com.antarescraft.kloudy.hologui.guicomponents.ValueScrollerComponent;
import com.antarescraft.kloudy.hologui.handlers.ClickHandler;
import com.antarescraft.kloudy.hologui.playerguicomponents.PlayerGUIPage;
import com.antarescraft.kloudy.hologui.playerguicomponents.PlayerGUIPageModel;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.util.IOManager;

/**
 * Represents a data model for the logbook gui page
 */

public class LogbookPageModel extends PlayerGUIPageModel
{
	private PlayerGUIPage playerGUIPage;
	
	private ValueScrollerComponent dateScroller;
	private ButtonComponent logBtn;
	private ButtonComponent nextBtn;
	private ButtonComponent backBtn;
	
	private int page;
	private Calendar date;
	private ArrayList<String> logLines;
	
	/**
	 * 
	 * @param guiPage
	 * @param player 
	 * @param staffMember Staff member whose logs are being looked up
	 */
	public LogbookPageModel(final HoloGUIPlugin plugin, final GUIPage guiPage, final Player player, final StaffMember staffMember)
	{
		super(plugin, guiPage, player);
		
		System.out.println("made it to logbook page model constructor");
		
		playerGUIPage = PlayerData.getPlayerData(player).getPlayerGUIPage();
				
		dateScroller = (ValueScrollerComponent) guiPage.getComponent("date-value-scroller");
		logBtn = (ButtonComponent)guiPage.getComponent("log-btn");
		nextBtn = (ButtonComponent)guiPage.getComponent("next-page-btn");
		backBtn = (ButtonComponent)guiPage.getComponent("prev-page-btn");
		
		page = 0;
		date = (Calendar)dateScroller.getPlayerScrollValue(player).getValue();
		
		logLines = IOManager.getLogFile(staffMember, date);
		if(logLines != null && logLines.size() > 10)
		{
			playerGUIPage.renderComponent(nextBtn);
		}
		
		//find logs click handler
		logBtn.registerClickHandler(new ClickHandler()
		{
			@Override
			public void onClick()
			{				
				playerGUIPage.removeComponent("log-label");
				
				date = (Calendar)dateScroller.getPlayerScrollValue(player).getValue();
				logLines = IOManager.getLogFile(staffMember, date);
								
				renderLogPageLabel();
			}
		});
		
		//next log page click handler
		nextBtn.registerClickHandler(new ClickHandler()
		{
			@Override
			public void onClick()
			{
				playerGUIPage.removeComponent("log-label");
				
				page++;
				
				if(backBtn.isHidden())
				{
					playerGUIPage.renderComponent(backBtn);
				}
				
				if(page == logLines.size() / 10)
				{
					playerGUIPage.removeComponent("next-page-btn");
				}
				
				renderLogPageLabel();
			}
		});
		
		//previous log page click handler
		backBtn.registerClickHandler(new ClickHandler()
		{
			@Override
			public void onClick()
			{
				playerGUIPage.removeComponent("log-label");
				
				page--;
				
				if(page == 0)
				{
					playerGUIPage.removeComponent("prev-page-btn");
				}
				
				if(nextBtn.isHidden())
				{
					playerGUIPage.renderComponent(nextBtn);
				}
				
				renderLogPageLabel();
			}
		});
	}
	
	public int getCurrentPage()
	{
		return page;
	}
	
	private void renderLogPageLabel()
	{		
		if(logLines == null) return;
		
		GUIComponentProperties properties = new GUIComponentProperties(plugin, "log-label", guiPage.getId(), new ComponentPosition(0, 0.04),
				null, 10, false, false);
		
		String[] logPage = new String[20];
		Arrays.fill(logPage, "");
		
		for(int i = 0; i < 10; i++)
		{
			if((page*10) + i >= logLines.size()) break;
			
			logPage[i*2] = logLines.get((page*10) + i);
		}
		
		LabelComponent logLabel = new LabelComponent(properties, logPage);
		playerGUIPage.renderComponent(logLabel);
	}
}