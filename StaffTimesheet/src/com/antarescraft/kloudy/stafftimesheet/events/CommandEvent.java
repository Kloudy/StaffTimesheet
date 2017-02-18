package com.antarescraft.kloudy.stafftimesheet.events;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologuiapi.plugincore.command.CommandHandler;
import com.antarescraft.kloudy.hologuiapi.plugincore.command.CommandParser;
import com.antarescraft.kloudy.hologuiapi.plugincore.messaging.MessageManager;
import com.antarescraft.kloudy.stafftimesheet.ShiftManager;
import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;
import com.antarescraft.kloudy.stafftimesheet.config.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.config.StaffTimesheetConfig;
import com.antarescraft.kloudy.stafftimesheet.datamodels.AdminTimesheetHomePageModel;
import com.antarescraft.kloudy.stafftimesheet.datamodels.TimesheetHomePageModel;

import net.md_5.bungee.api.ChatColor;

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
		
		if(StaffTimesheetConfig.debugMode)
		{
			String definedStaffMsg = (String.format(ChatColor.GOLD + "[%s]: \nAttempting to open Staff Member GUI: \nPlayer { name: %s, uuid: %s, staff-member-defined: %b } ", 
					staffTimesheet.getName(), player.getName(), player.getUniqueId(), staffMember != null));
		
			definedStaffMsg += String.format("\n\n%d defined staff members exist in staff-members.yml: \n\n", 
					config.getStaffMembersConfig().getAllStaffMembers().size());
			
			for(StaffMember definedStaffMember : config.getStaffMembersConfig().getAllStaffMembers())
			{
				definedStaffMsg += definedStaffMember.toString() + "\n";
			}
			
			Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + definedStaffMsg);
		}
		
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
				boolean success = shiftManager.clockIn(staffMember, config.getEventLabelConfig().getShiftStart());
				
				if(success)player.sendMessage(config.getShiftStartStopMessagesConfig().getShiftStart(staffMember));
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
}