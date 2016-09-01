package com.antarescraft.kloudy.stafftimesheet.events;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.plugincore.command.CommandHandler;
import com.antarescraft.kloudy.plugincore.command.CommandParser;
import com.antarescraft.kloudy.stafftimesheet.ShiftEndReason;
import com.antarescraft.kloudy.stafftimesheet.ShiftManager;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;
import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;

public class CommandEvent implements CommandExecutor
{
	private ConfigManager configManager;
	
	public CommandEvent(StaffTimesheet staffTimesheet, ConfigManager configManager)
	{
		this.configManager = configManager;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		return CommandParser.parseCommand(this, "staff", cmd.getName(), sender, args);
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
			ShiftManager.getInstance().clockIn(staffMember);
			
			player.sendMessage(configManager.getShiftStartMessage());
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
				sender.sendMessage(configManager.getEndShiftClockOutMessage());
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
			mustBePlayer = true, permission = "shift.admin", subcommands = "staff manage <player_name> reset")
	public void shiftAdminManageReset(CommandSender sender, String[] args)
	{
		//TODO
	}
	
	@CommandHandler(description = "Adds the specified amount of time to the specified staff member's time for the current month with format: [hh:mm:ss]", 
			mustBePlayer = true, permission = "shift.admin", subcommands = "staff manage <player_name> add <formatted_time>")
	public void shiftAdminManageAddTime(CommandSender sender, String[] args)
	{
		//TODO
	}
	
	@CommandHandler(description = "Subtracts the specified amount of time from the specified staff member's time for the current month with format: [hh:mm:ss]", 
			mustBePlayer = false, permission = "", subcommands = "staff manage <player_name> subtract <formatted_time>")
	public void shiftAdminManageSubtractTime(CommandSender sender, String[] args)
	{
		//TODO
	}
	
	@CommandHandler(description = "Gives a book containing the specified staff member's timecard log", 
			mustBePlayer = false, permission = "shift.admin", subcommands = "staff log <staff_member_player_name>")
	public void shiftAdminTimeCard(CommandSender sender, String[] args)
	{
		//TODO
	}
}