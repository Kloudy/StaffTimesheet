package com.antarescraft.kloudy.stafftimesheet.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.antarescraft.kloudy.hologui.events.HoloGUIClickEvent;
import com.antarescraft.kloudy.hologui.guicomponents.ClickableGUIComponent;
import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;

public class HoloGUIClickEventListener implements Listener
{
	private StaffTimesheet staffTimesheet;
	
	public HoloGUIClickEventListener(StaffTimesheet staffTimesheet)
	{
		this.staffTimesheet = staffTimesheet;
	}
	
	@EventHandler
	public void holoGUIClickEvent(HoloGUIClickEvent event)
	{
		ClickableGUIComponent clickedComponent = event.getClickedComponent();
		if(clickedComponent.getHoloGUIPlugin().getName().equals(staffTimesheet.getName()))
		{
			if(clickedComponent.getContainerId().equals("timesheet-home"))
			{
				{
					staffTimesheet.getHoloGUI().openGUIPage(staffTimesheet, event.getPlayer(), "timesheet-log");
				}
			}
		}
	}
}