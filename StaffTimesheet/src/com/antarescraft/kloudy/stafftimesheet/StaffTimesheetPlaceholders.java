package com.antarescraft.kloudy.stafftimesheet;

import java.util.Calendar;

import com.antarescraft.kloudy.hologuiapi.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.stafftimesheet.config.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.config.StaffMembersConfig;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.clip.placeholderapi.external.EZPlaceholderHook;
import net.md_5.bungee.api.ChatColor;

public class StaffTimesheetPlaceholders extends EZPlaceholderHook
{
	private StaffMembersConfig staffConfig;
	
	public StaffTimesheetPlaceholders(Plugin plugin, String identifier, StaffMembersConfig staffConfig)
	{
		super(plugin, identifier);
		
		this.staffConfig = staffConfig;
	}

	@Override
	public String onPlaceholderRequest(Player player, String identifier) 
	{
		String errorMessage = ChatColor.RED + "" + ChatColor.BOLD + "Staff member not found";
		if(player == null) return errorMessage;
		
		//Some placeholders can have parameters optionally appended: <placeholder>_<secondary_param>
		String playerName = getSecondaryPlaceholderParameter(identifier);//playerName passed as secondary placeholder parameter
		StaffMember staffMember = null;
		
		if(playerName != null)
		{
			staffMember = staffConfig.getStaffMember(playerName);
		}
		else
		{
			staffMember = staffConfig.getStaffMember(player);
		}
		
		if(staffMember == null) return errorMessage;
		
		if(identifier.equals("staff-member-name"))
		{
			return staffMember.getPlayerName();
		}
		else if(identifier.matches("current-logged-time(_.+)?"))
		{
			return staffMember.getLoggedTimeString();
		}
		else if(identifier.matches("time-goal(_.+)?"))
		{
			return staffMember.getTimeGoalString();
		}
		else if(identifier.matches("percent-time-logged(_.+)?"))
		{
			return Double.toString(staffMember.getPercentageTimeCompleted()) + "%";
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
		
		return errorMessage;
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