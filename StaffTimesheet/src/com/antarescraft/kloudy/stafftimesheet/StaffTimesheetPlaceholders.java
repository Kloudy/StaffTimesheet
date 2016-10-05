package com.antarescraft.kloudy.stafftimesheet;

import java.util.Calendar;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.antarescraft.kloudy.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;

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

		if(identifier.equals("staff-member-name"))
		{
			StaffMember staffMember = configManager.getStaffMember(player);
			if(staffMember != null) return staffMember.getPlayerName();
		}
		//These placeholders can have player name optionally appended: <placeholder>_player-name
		else if(identifier.matches("current-logged-time(_.+)?"))
		{
			String playerName = getSecondaryPlaceholderParameter(identifier);
			if(playerName != null)
			{
				StaffMember staffMember = configManager.getStaffMember(playerName);
				if(staffMember != null) return staffMember.getLoggedTimeString();
			}
			else
			{
				StaffMember staffMember = configManager.getStaffMember(player);
				if(staffMember != null) return staffMember.getLoggedTimeString();
			}
		}
		else if(identifier.matches("time-goal(_.+)?"))
		{
			return staffMember.getTimeGoal();
		}
		else if(identifier.matches("percent-time-logged(_.+)?"))
		{
			return Double.toString(staffMember.getPercentageTimeCompleted());
		}
		else if(identifier.matches("clocked-in(_.+)?"))
		{
			if(ShiftManager.getInstance().onTheClock(staffMember))
			{
				return "Yes";
			}
			return "No";
		}
		//end placeholders that take optional playername parameter
		else if(identifier.equals("default-date"))
		{
			Calendar now = Calendar.getInstance();
			return TimeFormat.getDateFormat(now);
		}
		
		return "";
	}
	
	private String getSecondaryPlaceholderParameter(String placeholder)
	{
		String[] placeholderTokens = placeholder.split("_");
		if(placeholderTokens.length == 2)
		{
			return placeholderTokens[1];
		}
		else
		{
			return null;
		}
	}
}