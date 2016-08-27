package com.antarescraft.kloudy.stafftimesheet.events;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.antarescraft.kloudy.plugincore.command.CommandHandler;
import com.antarescraft.kloudy.plugincore.command.CommandParser;
import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;

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
		return CommandParser.parseCommand(this, staffTimesheet, "staff", cmd.getName(), sender, args);
	}
	
	@CommandHandler(description = "Starts the shift for the staff member", mustBePlayer = true, permission = "shift.staff", subcommands = "start")
	public void shiftStart(CommandSender sender, String[] args)
	{
		//TODO
	}
	
	@CommandHandler(description = "Ends the shift for the staff member", mustBePlayer = false, permission = "shift.staff", subcommands = "end")
	public void shiftEnd(CommandSender sender, String[] args)
	{
		//TODO
	}
	
	@CommandHandler(description = "Resets a staff memeber's time for the current month", 
			mustBePlayer = true, permission = "shift.admin", subcommands = "shift admin manage <player_name> reset")
	public void shiftAdminManageReset(CommandSender sender, String[] args)
	{
		//TODO
	}
	
	@CommandHandler(description = "Adds the specified amount of time to the specified staff member's time for the current month with format: [hh:mm:ss]", 
			mustBePlayer = true, permission = "shift.admin", subcommands = "shift admin manage <player_name> add <formatted_time>")
	public void shiftAdminManageAddTime(CommandSender sender, String[] args)
	{
		//TODO
	}
	
	@CommandHandler(description = "Subtracts the specified amount of time from the specified staff member's time for the current month with format: [hh:mm:ss]", 
			mustBePlayer = false, permission = "", subcommands = "shift admin manager <player_name> subtract <formatted_time>")
	public void shiftAdminManageSubtractTime(CommandSender sender, String[] args)
	{
		//TODO
	}
	
	@CommandHandler(description = "Gives a book containing the specified staff member's timecard log", 
			mustBePlayer = false, permission = "shift.admin", subcommands = "shift admin log <staff_member_player_name>")
	public void shiftAdminTimeCard(CommandSender sender, String[] args)
	{
		//TODO
	}
}