package com.antarescraft.kloudy.stafftimesheet;

import java.util.ArrayList;
import java.util.Calendar;

import com.antarescraft.kloudy.hologui.CallbackTrigger;
import com.antarescraft.kloudy.hologui.guicomponents.GUIComponent;
import com.antarescraft.kloudy.hologui.guicomponents.LabelComponent;
import com.antarescraft.kloudy.hologui.plugincore.exceptions.InvalidDateFormatException;
import com.antarescraft.kloudy.hologui.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;
import com.antarescraft.kloudy.stafftimesheet.util.IOManager;

public class HoloGUICallbackFunctions implements CallbackTrigger
{
	private StaffTimesheet staffTimesheet;
	private ConfigManager configManager;
	
	/**
	 * Class of callback functions to be called from click events from HoloGUI
	 */
	public HoloGUICallbackFunctions(StaffTimesheet staffTimesheet, ConfigManager configManager)
	{
		this.staffTimesheet = staffTimesheet;
		this.configManager = configManager;
	}
	
	/**
	 * Displays the contents of a log file into a label component
	 * @param guiContainerId guiContainerId of the label
	 * @param logTextLabelId id of the label
	 * @param playerName The name of the staff member whose log is being looked up
	 * @param dateFormat Date format of the date of the log file
	 */
	public void displayStaffMemberLog(String guiContainerId, String logTextLabelId, String playerName, String dateFormat)
	{	
		if(guiContainerId == null || logTextLabelId == null || playerName == null || dateFormat == null) return;
		
		GUIComponent guiComponent = staffTimesheet.getGUIComponent(guiContainerId, logTextLabelId);
		if(guiComponent != null && !(guiComponent instanceof LabelComponent)) return;
		
		LabelComponent labelComponent = (LabelComponent)guiComponent;

		System.out.println("Made it - label not null");
		StaffMember staffMember = configManager.getStaffMember(playerName);
		if(staffMember == null) return;
		System.out.println("Made it - staff member not null");
		try
		{
			Calendar date = TimeFormat.parseDateFormat(dateFormat);
			ArrayList<String> logLines = IOManager.getLogFile(staffMember, date);
			if(logLines == null) return;
			
			System.out.println("logLines not null");
			
			String[] lines = new String[logLines.size()];
			logLines.toArray(lines);
			
			System.out.println(logLines.size());
			
			labelComponent.setLines(lines);
		}
		catch (InvalidDateFormatException e){}
	}
	
	
}