package com.antarescraft.kloudy.stafftimesheet.events;

import java.time.Duration;
import java.util.Calendar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.plugincore.command.CommandHandler;
import com.antarescraft.kloudy.plugincore.command.CommandParser;
import com.antarescraft.kloudy.stafftimesheet.ShiftEndReason;
import com.antarescraft.kloudy.stafftimesheet.ShiftManager;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.StaffMemberLogbook;
import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;
import com.antarescraft.kloudy.stafftimesheet.exceptions.InvalidDateFormatException;
import com.antarescraft.kloudy.stafftimesheet.exceptions.InvalidDurationFormatException;
import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;
import com.antarescraft.kloudy.stafftimesheet.util.TimeFormat;

public class CommandEvent implements CommandExecutor
{
	private StaffTimesheet staffTimesheet;
	private ConfigManager configManager;
	
	public CommandEvent(StaffTimesheet staffTimesheet, ConfigManager configManager)
	{
		this.staffTimesheet = staffTimesheet;
		this.configManager = configManager;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		return CommandParser.parseCommand(staffTimesheet, this, "staff", cmd.getName(), sender, args);
	}
	
	@CommandHandler(description = "Reloads the values contained in the config file",
			mustBePlayer = false, permission = "staff.admin", subcommands = "reloadconfig")
	public void reloadConfig()
	{
		configManager.loadConfigValues();
	}
	
	@CommandHandler(description = "Starts the shift for the staff member", 
			mustBePlayer = true, permission = "shift.staff", subcommands = "shift clockin")
	public void shiftStart(CommandSender sender, String[] args)
	{
		Player player = (Player)sender;
		
		StaffMember staffMember = configManager.getStaffMember(player);
		if(staffMember != null)
		{
			staffMember.logEntry(configManager.getShiftStartLabel());
			
			ShiftManager.getInstance().clockIn(staffMember);
			
			player.sendMessage(configManager.getShiftStartMessage(staffMember));
		}
		else
		{
			sender.sendMessage(configManager.getErrorMessageNotStaff());
		}
	}
	
	@CommandHandler(description = "Ends the shift for the staff member",
			mustBePlayer = true, permission = "shift.staff", subcommands = "shift clockout")
	public void shiftEnd(CommandSender sender, String[] args)
	{
		Player player = (Player)sender;
		ShiftManager shiftManager = ShiftManager.getInstance();
		
		StaffMember staffMember = configManager.getStaffMember(player);
		if(staffMember != null)
		{
			if(shiftManager.onTheClock(staffMember))
			{
				shiftManager.clockOut(staffMember, ShiftEndReason.CLOCKED_OUT);
				sender.sendMessage(configManager.getEndShiftClockOutMessage(staffMember));
				
				staffMember.logEntry(configManager.getShiftLabelClockedOut());
			}
			else
			{
				sender.sendMessage(configManager.getErrorMessageNotClockedIn());
			}
		}
		else
		{
			sender.sendMessage(configManager.getErrorMessageNotStaff());
		}
	}
	
	@CommandHandler(description = "Resets a staff memeber's time for the current month", 
			mustBePlayer = true, permission = "shift.admin", subcommands = "manage <player_name> reset")
	public void resetStaffMemberTime(CommandSender sender, String[] args)
	{
		StaffMember staffMember = configManager.getStaffMember(args[2]);
		if(staffMember != null)
		{
			staffMember.resetLoggedTime();
			
			sender.sendMessage(configManager.getResetStaffMemberLoggedTimeMessage());
		}
		else
		{
			sender.sendMessage(configManager.getErrorMessageStaffMemberDoesNotExist());
		}
	}
	
	@CommandHandler(description = "Adds the specified amount of time to the specified staff member's time for the current month with format: [hh:mm:ss]", 
			mustBePlayer = true, permission = "shift.admin", subcommands = "manage <player_name> add <formatted_time>")
	public void addStaffMemberTime(CommandSender sender, String[] args)
	{
		StaffMember staffMember = configManager.getStaffMember(args[2]);
		if(staffMember != null)
		{
			try
			{
				Duration time = TimeFormat.parseTimeFormat(args[4]);
				staffMember.addLoggedTime(time);
				
				sender.sendMessage(configManager.getAddLoggedTimeForStaffMemberMessage());
			}
			catch(InvalidDurationFormatException e)
			{
				sender.sendMessage(configManager.getErrorMessageInvalidDurationFormat());
			}
		}
		else
		{
			sender.sendMessage(configManager.getErrorMessageStaffMemberDoesNotExist());
		}
	}
	
	@CommandHandler(description = "Subtracts the specified amount of time from the specified staff member's time for the current month with format: [hh:mm:ss]", 
			mustBePlayer = false, permission = "", subcommands = "manage <player_name> subtract <formatted_time>")
	public void shiftAdminManageSubtractTime(CommandSender sender, String[] args)
	{
		StaffMember staffMember = configManager.getStaffMember(args[2]);
		if(staffMember != null)
		{
			try
			{
				Duration time = TimeFormat.parseTimeFormat(args[4]);
				staffMember.subtractLoggedTime(time);
				
				sender.sendMessage(configManager.getSubtractLoggedTimeForStaffMemberMessage());
			}
			catch(InvalidDurationFormatException e)
			{
				sender.sendMessage(configManager.getErrorMessageInvalidDurationFormat());
			}
		}
		else
		{
			sender.sendMessage(configManager.getErrorMessageStaffMemberDoesNotExist());
		}
	}
	
	@CommandHandler(description = "Gives a book containing the specified staff member's timecard log. Dates have format: yyyy/mm/dd. If no end date is specified the end date becomes the current date", 
			mustBePlayer = true, permission = "shift.admin", subcommands = "logbook <staff_member_player_name> <start_date> [end_date]")
	public void staffLogbook(CommandSender sender, String[] args)
	{
		Player player = (Player)sender;
		
		Calendar startDate = null;
		Calendar endDate = null;
		
		try
		{
			startDate = TimeFormat.parseDateFormat(args[2]);
			
			if(args.length == 3)//no end date specified, make the end date be today
			{
				endDate = Calendar.getInstance();
			}
			else
			{
				endDate = TimeFormat.parseDateFormat(args[3]);
			}
		}
		catch(InvalidDateFormatException e)
		{
			sender.sendMessage(configManager.getErrorMessageInvalidDateFormat());
		}
				
		if(startDate.compareTo(endDate) <= 0)
		{
			StaffMember staffMember = configManager.getStaffMember(args[1]);
			if(staffMember != null)
			{
				sender.sendMessage(configManager.getLoadingStaffMemberLogbookMessage());
				
				StaffMemberLogbook logbook = new StaffMemberLogbook(staffMember, startDate, endDate);
				logbook.getLogbook(staffTimesheet, player, configManager.getMaxLogRange(), configManager.getLoadedStaffMemberLogbookMessage());
			}
			else
			{
				sender.sendMessage(configManager.getErrorMessageStaffMemberDoesNotExist());
			}
		}
		else
		{
			sender.sendMessage(configManager.getErrorMessageStartDateEndDateMismatch());
		}
	}
}