package com.antarescraft.kloudy.stafftimesheet;

import java.util.Calendar;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;
import com.antarescraft.kloudy.stafftimesheet.util.TimeFormat;

import me.clip.placeholderapi.external.EZPlaceholderHook;

public class StaffTimesheetPlaceholders extends EZPlaceholderHook
{
	private ConfigManager configManager;
	
	public StaffTimesheetPlaceholders(Plugin plugin, String identifier, ConfigManager configManager)
	{
		super(plugin, identifier);
		
		this.configManager = configManager;
	}

	@Override
	public String onPlaceholderRequest(Player player, String identifier) 
	{
		if(player == null) return "";
		
		StaffMember staffMember = configManager.getStaffMember(player);
		if(staffMember != null)
		{
			if(identifier.equals("staff-member-name"))
			{
				return staffMember.getPlayerName();
			}
			else if(identifier.equals("current-logged-time"))
			{
				return staffMember.getLoggedTimeString();
			}
			else if(identifier.equals("time-goal"))
			{
				return staffMember.getTimeGoal();
			}
			else if(identifier.equals("percent-time-logged"))
			{
				return Double.toString(staffMember.getPercentageTimeCompleted());
			}
			else if(identifier.equals("clocked-in"))
			{
				if(ShiftManager.getInstance().onTheClock(staffMember))
				{
					return "Yes";
				}
				return "No";
			}
			else if(identifier.equals("default-start-time") || identifier.equals("default-end-time"))
			{
				Calendar now = Calendar.getInstance();
				return TimeFormat.getDateFormat(now);
			}
		}
		
		return "";
	}
}