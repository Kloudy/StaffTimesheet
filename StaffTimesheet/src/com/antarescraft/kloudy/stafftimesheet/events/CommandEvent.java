package com.antarescraft.kloudy.stafftimesheet.events;

import java.time.Duration;
import java.util.Calendar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.plugincore.command.CommandHandler;
import com.antarescraft.kloudy.plugincore.command.CommandParser;
import com.antarescraft.kloudy.plugincore.exceptions.DurationOverflowException;
import com.antarescraft.kloudy.plugincore.exceptions.DurationUnderflowException;
import com.antarescraft.kloudy.plugincore.exceptions.InvalidDateFormatException;
import com.antarescraft.kloudy.plugincore.exceptions.InvalidDurationFormatException;
import com.antarescraft.kloudy.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.stafftimesheet.ShiftEndReason;
import com.antarescraft.kloudy.stafftimesheet.ShiftManager;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.StaffMemberLogbook;
import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;
import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;

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
	public void reloadConfig(CommandSender sender, String[] args)
	{
		staffTimesheet.loadGUIContainersFromYaml(sender);
		configManager.loadConfigValues();
	}
	
	@CommandHandler(description = "Opens the Staff Timesheet HoloGUI menu",
			mustBePlayer = true, permission = "staff.shift", subcommands = "menu")
	public void openMenu(CommandSender sender, String[] args)
	{
		staffTimesheet.getHoloGUI().openGUIPage(staffTimesheet, (Player)sender, "timesheet-home");
	}
	
	@CommandHandler(description = "Starts the shift for the staff member", 
			mustBePlayer = true, permission = "staff.shift", subcommands = "clockin")
	public void shiftStart(CommandSender sender, String[] args)
	{
		Player player = (Player)sender;
		
		StaffMember staffMember = configManager.getStaffMember(player);
		if(staffMember != null)
		{
			ShiftManager shiftManager = ShiftManager.getInstance();
			
			if(!shiftManager.onTheClock(staffMember))
			{
				staffMember.logEntry(configManager.getShiftStartLabel());

				shiftManager.clockIn(staffMember);
				
				player.sendMessage(configManager.getShiftStartMessage(staffMember));
			}
			else
			{
				player.sendMessage(configManager.getErrorMessageAlreadyClockedIn());
			}
		}
		else
		{
			sender.sendMessage(configManager.getErrorMessageNotStaff());
		}
	}
	
	@CommandHandler(description = "Ends the shift for the staff member",
			mustBePlayer = true, permission = "staff.shift", subcommands = "clockout")
	public void shiftEnd(CommandSender sender, String[] args)
	{
		Player player = (Player)sender;
		
		StaffMember staffMember = configManager.getStaffMember(player);
		if(staffMember != null)
		{
			ShiftManager shiftManager = ShiftManager.getInstance();

			if(shiftManager.onTheClock(staffMember))
			{
				shiftManager.clockOut(staffMember, ShiftEndReason.CLOCKED_OUT);
				sender.sendMessage(configManager.getShiftEndClockoutMessage(staffMember));
				
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
			mustBePlayer = true, permission = "staff.admin", subcommands = "manage <player_name> reset")
	public void resetStaffMemberTime(CommandSender sender, String[] args)
	{
		StaffMember staffMember = configManager.getStaffMember(args[1]);
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
			mustBePlayer = true, permission = "staff.admin", subcommands = "manage <player_name> add <formatted_time>")
	public void addStaffMemberTime(CommandSender sender, String[] args)
	{
		StaffMember staffMember = configManager.getStaffMember(args[1]);
		if(staffMember != null)
		{
			try
			{
				Duration time = TimeFormat.parseDurationFormat(args[3]);
				
				try
				{
					staffMember.addLoggedTime(time);
					
					sender.sendMessage(configManager.getAddLoggedTimeForStaffMemberMessage());
				}
				catch(DurationOverflowException e)//trying to add more time than is allowed, set to max duration
				{
					sender.sendMessage(configManager.getErrorMessageDurationOverflow());
				}
				
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
			mustBePlayer = false, permission = "staff.admin", subcommands = "manage <player_name> subtract <formatted_time>")
	public void shiftAdminManageSubtractTime(CommandSender sender, String[] args)
	{
		StaffMember staffMember = configManager.getStaffMember(args[1]);
		if(staffMember != null)
		{
			try
			{
				Duration time = TimeFormat.parseDurationFormat(args[3]);
				if(time.compareTo(staffMember.getLoggedTime()) <= 0)
				{
					try
					{
						staffMember.subtractLoggedTime(time);
						
						sender.sendMessage(configManager.getSubtractLoggedTimeForStaffMemberMessage());
					}
					catch(DurationUnderflowException e)//trying to subtract more time than has been logged, set to min duration
					{
						sender.sendMessage(configManager.getErrorMessageDurationUnderflow());
					}
				}
				else//trying to subtract more time than the staff member already has logged
				{
					sender.sendMessage(configManager.getErrorMessageDurationUnderflow());
				}
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
			mustBePlayer = true, permission = "staff.admin", subcommands = "logbook <staff_member_player_name> <start_date> [end_date]")
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