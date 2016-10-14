package com.antarescraft.kloudy.stafftimesheet.datamodels;

import java.util.ArrayList;
import java.util.Calendar;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologui.HoloGUIPlugin;
import com.antarescraft.kloudy.hologui.PlayerData;
import com.antarescraft.kloudy.hologui.guicomponents.AbstractIncrementableValue;
import com.antarescraft.kloudy.hologui.guicomponents.ButtonComponent;
import com.antarescraft.kloudy.hologui.guicomponents.ComponentPosition;
import com.antarescraft.kloudy.hologui.guicomponents.GUIComponentProperties;
import com.antarescraft.kloudy.hologui.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologui.guicomponents.LabelComponent;
import com.antarescraft.kloudy.hologui.guicomponents.ValueScrollerComponent;
import com.antarescraft.kloudy.hologui.handlers.ClickHandler;
import com.antarescraft.kloudy.hologui.handlers.ScrollHandler;
import com.antarescraft.kloudy.hologui.playerguicomponents.PlayerGUIComponent;
import com.antarescraft.kloudy.hologui.playerguicomponents.PlayerGUIPage;
import com.antarescraft.kloudy.hologui.playerguicomponents.PlayerGUIPageModel;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.util.IOManager;

/**
 * Represents a base data model for the logbook gui page
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
		
		playerGUIPage = PlayerData.getPlayerData(player).getPlayerGUIPage();
		
		this.staffMember = staffMember;
		
		dateScroller = (ValueScrollerComponent) guiPage.getComponent("date-value-scroller");
		logBtn = (ButtonComponent)guiPage.getComponent("log-btn");
		nextBtn = (ButtonComponent)guiPage.getComponent("next-page-btn");
		backBtn = (ButtonComponent)guiPage.getComponent("prev-page-btn");
		
		page = 0;
		date = (Calendar)dateScroller.getPlayerScrollValue(player).getValue();
		
		logLines = getLogStrings();
		if(logLines == null) return;
		
		//find logs click handler
		logBtn.registerClickHandler(new ClickHandler()
		{
			@Override
			public void onClick()
			{
				Calendar date = (Calendar)dateScroller.getPlayerScrollValue(player).getValue();
				
				renderLogPageLabel();
			}
		});
		
		nextBtn.registerClickHandler(new ClickHandler()
		{
			@Override
			public void onClick()
			{
				page++;
				
				renderLogPageLabel();
			}
		});
		
		backBtn.registerClickHandler(new ClickHandler()
		{
			@Override
			public void onClick()
			{
				page--;
				
				renderLogPageLabel();
			}
		});
		
		dateScroller.registerScrollHandler(new ScrollHandler()
		{
			@Override
			public void onScroll(AbstractIncrementableValue<?, ?> value)
			{
				date = (Calendar)value.getValue();
			}
		});
	}
	
	public int getCurrentPage()
	{
		return page;
	}
	
	private void renderLogPageLabel()
	{
		ArrayList<String> logs = IOManager.getLogFile(staffMember, date);
		
		GUIComponentProperties properties = new GUIComponentProperties(plugin, "log-label-" + page, guiPage.getId(), new ComponentPosition(0, 0.04),
				null, 10, false, false);
		
		String[] logLines = new String[10];
		
		LabelComponent logLabel = new LabelComponent(properties, logLines);
		playerGUIPage.renderComponent(logLabel);
	}
	
	private String[] getLogPage()
	{
		String[] logPage = new String[10];
		int index = page * 10;
		for(int i = 0; i < 10; i++)
		{
			if(index < logLines.size())
			{
				logPage[i] = logLines.get(index);
				index++;
			}
		}
		
		return logPage;
	}
	
	private ArrayList<String> getLogStrings()
	{
		return IOManager.getLogFile(staffMember, date);
	}
}