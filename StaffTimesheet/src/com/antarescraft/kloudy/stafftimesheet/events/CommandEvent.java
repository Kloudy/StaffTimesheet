package com.antarescraft.kloudy.stafftimesheet.events;

import java.time.Duration;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologuiapi.plugincore.command.CommandHandler;
import com.antarescraft.kloudy.hologuiapi.plugincore.command.CommandParser;
import com.antarescraft.kloudy.hologuiapi.plugincore.exceptions.DurationOverflowException;
import com.antarescraft.kloudy.hologuiapi.plugincore.exceptions.DurationUnderflowException;
import com.antarescraft.kloudy.hologuiapi.plugincore.exceptions.InvalidDurationFormatException;
import com.antarescraft.kloudy.hologuiapi.plugincore.messaging.MessageManager;
import com.antarescraft.kloudy.hologuiapi.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.stafftimesheet.ShiftManager;
import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;
import com.antarescraft.kloudy.stafftimesheet.config.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.config.StaffTimesheetConfig;
import com.antarescraft.kloudy.stafftimesheet.datamodels.AdminTimesheetHomePageModel;
import com.antarescraft.kloudy.stafftimesheet.datamodels.TimesheetHomePageModel;

public class CommandEvent implements CommandExecutor
{
	private StaffTimesheet staffTimesheet;
	
	public CommandEvent(StaffTimesheet staffTimesheet)
	{
		this.staffTimesheet = staffTimesheet;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		return CommandParser.parseCommand(staffTimesheet, this, "staff", cmd.getName(), sender, args);
	}
	
	@CommandHandler(description = "Reloads the values contained in the config file",
			mustBePlayer = false, permission = "staff.admin", subcommands = "reload")
	public void reload(CommandSender sender, String[] args)
	{
		staffTimesheet.getHoloGUIApi().destroyGUIPages(staffTimesheet);
		
		StaffTimesheetConfig.loadConfig(staffTimesheet);
		
		MessageManager.info(sender, "Config values reloaded.");
	}
	
	@CommandHandler(description = "Opens the Staff Timesheet HoloGUI menu",
			mustBePlayer = true, permission = "staff.shift", subcommands = "menu")
	public void openMenu(CommandSender sender, String[] args)
	{
		Player player = (Player)sender;
		
		StaffTimesheetConfig config = StaffTimesheetConfig.getConfig(staffTimesheet);
		
		StaffMember staffMember = config.getStaffMembersConfig().getStaffMember(player);
		
		if(staffMember != null)
		{
			if(staffMember.isSuperAdmin())
			{
				AdminTimesheetHomePageModel model = new AdminTimesheetHomePageModel(staffTimesheet, staffTimesheet.getGUIPages().get("timesheet-home-admin"), player);
				staffTimesheet.getHoloGUIApi().openGUIPage(staffTimesheet, player, "timesheet-home-admin", model);
			}
			else
			{
				TimesheetHomePageModel model = new TimesheetHomePageModel(staffTimesheet, staffTimesheet.getGUIPages().get("timesheet-home"), player);
				staffTimesheet.getHoloGUIApi().openGUIPage(staffTimesheet, player, "timesheet-home", model);
			}
		}
		else
		{
			sender.sendMessage(config.getErrorMessageConfig().getNotStaff());
		}
	}
	
	@CommandHandler(description = "Starts the shift for the staff member", 
			mustBePlayer = true, permission = "staff.shift", subcommands = "clockin")
	public void shiftStart(CommandSender sender, String[] args)
	{
		Player player = (Player)sender;
		
		StaffTimesheetConfig config = StaffTimesheetConfig.getConfig(staffTimesheet);
		
		StaffMember staffMember = config.getStaffMembersConfig().getStaffMember(player);
		if(staffMember != null)
		{
			ShiftManager shiftManager = ShiftManager.getInstance();
			
			if(!shiftManager.onTheClock(staffMember))
			{
				shiftManager.clockIn(staffMember, config.getEventLabelConfig().getShiftStart());
				
				player.sendMessage(config.getShiftStartStopMessagesConfig().getShiftStart(staffMember));
			}
			else
			{
				player.sendMessage(config.getErrorMessageConfig().getAlreadyClockedIn());
			}
		}
		else
		{
			sender.sendMessage(config.getErrorMessageConfig().getNotStaff());
		}
	}
	
	@CommandHandler(description = "Ends the shift for the staff member",
			mustBePlayer = true, permission = "staff.shift", subcommands = "clockout")
	public void shiftEnd(CommandSender sender, String[] args)
	{
		Player player = (Player)sender;
		
		StaffTimesheetConfig config = StaffTimesheetConfig.getConfig(staffTimesheet);
		
		StaffMember staffMember = config.getStaffMembersConfig().getStaffMember(player);
		if(staffMember != null)
		{
			ShiftManager shiftManager = ShiftManager.getInstance();

			if(shiftManager.onTheClock(staffMember))
			{
				shiftManager.clockOut(staffMember, config.getEventLabelConfig().getShiftEndClockout());
				sender.sendMessage(config.getShiftStartStopMessagesConfig().getShiftEndClockout(staffMember));				
			}
			else
			{
				sender.sendMessage(config.getErrorMessageConfig().getNotClockedIn());
			}
		}
		else
		{
			sender.sendMessage(config.getErrorMessageConfig().getNotStaff());
		}
	}
	
	@CommandHandler(description = "Resets a staff memeber's time for the current month", 
			mustBePlayer = true, permission = "staff.admin", subcommands = "manage <player_name> reset")
	public void resetStaffMemberTime(CommandSender sender, String[] args)
	{
		StaffTimesheetConfig config = StaffTimesheetConfig.getConfig(staffTimesheet);
		
		StaffMember staffMember = config.getStaffMembersConfig().getStaffMember(args[1]);
		if(staffMember != null)
		{
			staffMember.resetLoggedTime();
			
			sender.sendMessage(config.getCommandResultMessageConfig().getResetStaffMemberLoggedTime());
		}
		else
		{
			sender.sendMessage(config.getErrorMessageConfig().getStaffMemberDoesNotExist());
		}
	}
	
	@CommandHandler(description = "Adds the specified amount of time to the specified staff member's time for the current month with format: [hh:mm:ss]", 
			mustBePlayer = true, permission = "staff.admin", subcommands = "manage <player_name> add <formatted_time>")
	public void addStaffMemberTime(CommandSender sender, String[] args)
	{
		StaffTimesheetConfig config = StaffTimesheetConfig.getConfig(staffTimesheet);
		
		StaffMember staffMember = config.getStaffMembersConfig().getStaffMember(args[1]);
		if(staffMember != null)
		{
			try
			{
				Duration time = TimeFormat.parseDurationFormat(args[3]);
				
				try
				{
					staffMember.addLoggedTime(time);
					
					sender.sendMessage(config.getCommandResultMessageConfig().getAddLoggedTimeForStaffMember());
				}
				catch(DurationOverflowException e)//trying to add more time than is allowed, set to max duration
				{
					sender.sendMessage(config.getErrorMessageConfig().getDurationOverflow());
				}
				
			}
			catch(InvalidDurationFormatException e)
			{
				sender.sendMessage(config.getErrorMessageConfig().getInvalidDurationFormat());
			}
		}
		else
		{
			sender.sendMessage(config.getErrorMessageConfig().getStaffMemberDoesNotExist());
		}
	}
	
	@CommandHandler(description = "Subtracts the specified amount of time from the specified staff member's time for the current month with format: [hh:mm:ss]", 
			mustBePlayer = false, permission = "staff.admin", subcommands = "manage <player_name> subtract <formatted_time>")
	public void shiftAdminManageSubtractTime(CommandSender sender, String[] args)
	{
		StaffTimesheetConfig config = StaffTimesheetConfig.getConfig(staffTimesheet);
		
		StaffMember staffMember = config.getStaffMembersConfig().getStaffMember(args[1]);
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
						
						sender.sendMessage(config.getCommandResultMessageConfig().getSubtractLoggedTimeForStaffMember());
					}
					catch(DurationUnderflowException e)//trying to subtract more time than has been logged, set to min duration
					{
						sender.sendMessage(config.getErrorMessageConfig().getDurationUnderflow());
					}
				}
				else//trying to subtract more time than the staff member already has logged
				{
					sender.sendMessage(config.getErrorMessageConfig().getDurationUnderflow());
				}
			}
			catch(InvalidDurationFormatException e)
			{
				sender.sendMessage(config.getErrorMessageConfig().getInvalidDurationFormat());
			}
		}
		else
		{
			sender.sendMessage(config.getErrorMessageConfig().getStaffMemberDoesNotExist());
		}
	}
}