package com.antarescraft.kloudy.stafftimesheet.datamodels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologuiapi.guicomponentproperties.LabelComponentProperties;
import com.antarescraft.kloudy.hologuiapi.guicomponents.ButtonComponent;
import com.antarescraft.kloudy.hologuiapi.guicomponents.ComponentPosition;
import com.antarescraft.kloudy.hologuiapi.guicomponents.GUIComponentFactory;
import com.antarescraft.kloudy.hologuiapi.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologuiapi.guicomponents.ItemButtonComponent;
import com.antarescraft.kloudy.hologuiapi.guicomponents.LabelComponent;
import com.antarescraft.kloudy.hologuiapi.guicomponents.ValueScrollerComponent;
import com.antarescraft.kloudy.hologuiapi.handlers.ClickHandler;
import com.antarescraft.kloudy.hologuiapi.handlers.GUIPageLoadHandler;
import com.antarescraft.kloudy.hologuiapi.playerguicomponents.PlayerGUIPage;
import com.antarescraft.kloudy.hologuiapi.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.hologuiapi.scrollvalues.DateScrollValue;
import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;
import com.antarescraft.kloudy.stafftimesheet.config.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.util.IOManager;

/**
 * Represents a data model for the logbook gui page
 */

public class LogbookPageModel extends BaseStaffTimesheetPageModel
{
	private PlayerGUIPage playerGUIPage;
	
	private ValueScrollerComponent dateScroller;
	private ItemButtonComponent logBtn;
	private ButtonComponent nextBtn;
	private ButtonComponent backBtn;
	
	private int page;
	private int totalPages;
	private Calendar date;
	private ArrayList<String> logLines;
	
	/**
	 * 
	 * @param guiPage
	 * @param player 
	 * @param staffMember Staff member whose logs are being looked up
	 */
	public LogbookPageModel(final StaffTimesheet plugin, final GUIPage guiPage, final Player player, final StaffMember staffMember)
	{
		super(plugin, guiPage, player, staffMember);
				
		dateScroller = (ValueScrollerComponent) guiPage.getComponent("date-value-scroller");
		logBtn = (ItemButtonComponent)guiPage.getComponent("log-btn");
		nextBtn = (ButtonComponent)guiPage.getComponent("next-page-btn");
		backBtn = (ButtonComponent)guiPage.getComponent("prev-page-btn");
		
		page = 0;
		totalPages = 0;
		
		dateScroller.setPlayerScrollValue(player, new DateScrollValue(Calendar.getInstance(), TimeFormat.getMinDuration().plusDays(1), null, Calendar.getInstance(), false));
		
		date = (Calendar)dateScroller.getPlayerScrollValue(player).getValue();
		
		guiPage.registerPageLoadHandler(player, new GUIPageLoadHandler()
		{
			@Override
			public void onPageLoad(PlayerGUIPage loadedPlayerGUIPage)
			{
				playerGUIPage = loadedPlayerGUIPage;
				
				//find logs click handler
				logBtn.registerClickHandler(player, new ClickHandler()
				{
					@Override
					public void onClick()
					{				
						playerGUIPage.removeComponent("log-label");
						playerGUIPage.removeComponent("page-label");
						playerGUIPage.removeComponent("next-page-btn");
						playerGUIPage.removeComponent("prev-page-btn");
						
						date = (Calendar)dateScroller.getPlayerScrollValue(player).getValue();
						logLines = IOManager.getLogFile(staffMember, date);
						
						page = 0;
						if(logLines != null && logLines.size() > 10)
						{
							totalPages = logLines.size() / 10;
							
							playerGUIPage.renderComponent(nextBtn);
							
							if(logLines.size() > 0)
							{
								renderPageLabel();
							}
						}
						
						if(logLines == null)
						{
							logLines = new ArrayList<String>();
							logLines.add("&c&lSorry, no logs were found for " + staffMember.getPlayerName() + " on this date.");
						}
										
						renderLogPageLabel();
					}
				});
				
				//next log page click handler
				nextBtn.registerClickHandler(player, new ClickHandler()
				{
					@Override
					public void onClick()
					{
						playerGUIPage.removeComponent("log-label");
						
						page++;
						
						if(backBtn.getProperties().isHidden())
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
				backBtn.registerClickHandler(player, new ClickHandler()
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
						
						if(nextBtn.getProperties().isHidden())
						{
							playerGUIPage.renderComponent(nextBtn);
						}
						
						renderLogPageLabel();
					}
				});
			}
		});
	}
	
	public int getCurrentPage()
	{
		return page+1;
	}
	
	public int getTotalPages()
	{
		return totalPages+1;
	}
	
	private void renderLogPageLabel()
	{		
		if(logLines == null) return;
		
		LabelComponentProperties properties = new LabelComponentProperties();
		properties.setId("log-label");
		properties.setPosition(new ComponentPosition(0, 0.3));
		properties.setLabelDistance(10.0);
		
		String[] logPage = new String[20];
		Arrays.fill(logPage, "");
		
		for(int i = 0; i < 10; i++)
		{
			if((page*10) + i >= logLines.size()) break;
			
			logPage[i*2] = logLines.get((page*10) + i);
		}
		
		properties.setLines(new ArrayList<String>(Arrays.asList(logPage)));
		
		LabelComponent logLabel = GUIComponentFactory.createLabelComponent(plugin, properties);
		playerGUIPage.renderComponent(logLabel);
	}
	
	private void renderPageLabel()
	{
		LabelComponentProperties properties = new LabelComponentProperties();
		properties.setId("page-label");
		properties.setPosition(new ComponentPosition(0, -0.5));
		properties.setLabelDistance(10.0);
		properties.setLines(new ArrayList<String>());
		properties.getLines().add("&lPage: $model.getCurrentPage();/$model.getTotalPages();");
				
		LabelComponent pageLabel = GUIComponentFactory.createLabelComponent(plugin, properties);
		playerGUIPage.renderComponent(pageLabel);
	}
}