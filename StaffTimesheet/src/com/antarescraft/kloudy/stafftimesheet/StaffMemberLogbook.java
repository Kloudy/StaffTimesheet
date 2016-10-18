package com.antarescraft.kloudy.stafftimesheet;

import java.util.ArrayList;
import java.util.Calendar;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.antarescraft.kloudy.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.stafftimesheet.util.IOManager;

import net.md_5.bungee.api.ChatColor;

/**
 * Represents a Logbook for a staff member containing logs in the range of the a start and end date 
 */

public class StaffMemberLogbook
{
	private StaffMember staffMember;
	private Calendar startDate;
	private Calendar endDate;

	public StaffMemberLogbook(StaffMember staffMember, Calendar startDate, Calendar endDate)
	{
		this.staffMember = staffMember;		
		this.startDate = startDate;
		this.endDate = endDate;
	}

	/*
	 * Getter Functions
	 */
	
	/**
	 * Aysnc function that gathers all the logs within this StaffMemberLogbook's date range.
	 * After the aysnc IO is completed the player is given a book containing all the logs
	 * 
	 * @param player the player to whom the logbook will be given
	 * @return ArrayList<Log> containing all the logs in this StaffMemberLogbook's startDate and endDate
	 */
	public void getLogbook(final StaffTimesheet staffTimesheet, final Player player, final int maxLogRange, final String loadedStaffMemberLogbookMessage)
	{
		new BukkitRunnable()//Async thread
		{
			@Override
			public void run()
			{
				final ArrayList<Log> logs = new ArrayList<Log>();
				
				//iterate over date range and add collect log files in the range
				Calendar date = (Calendar)startDate.clone();
				int count = 0;
				while(date.compareTo(endDate) <= 0 && count < maxLogRange)
				{
					ArrayList<String> lines = IOManager.getLogFile(staffMember, date);
					
					if(lines != null)
					{
						logs.add(new Log((Calendar)date.clone(), lines));
					}
					
					date.add(Calendar.DATE, 1);
					
					count++;
				}
				
				new BukkitRunnable()//Sync thread
				{
					@Override
					public void run()
					{
						ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
						BookMeta bookMeta = (BookMeta)book.getItemMeta();
						
						bookMeta.setTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "Staff Logbook: " + staffMember.getPlayerName());
						
						ArrayList<String> loreStrings = new ArrayList<String>();
						loreStrings.add(TimeFormat.getDateFormat(startDate) + " - " + TimeFormat.getDateFormat(endDate));
						loreStrings.add(staffMember.getPercentageTimeCompleted() + "% of time goal logged");
						bookMeta.setLore(loreStrings);
						
						ArrayList<String> bookPages = new ArrayList<String>();

						bookPages.add(firstPage());
						
						for(int i = 0; i < logs.size(); i++)
						{
							Log log = logs.get(i);
							
							StringBuilder strBuilder = new StringBuilder();
														
							strBuilder.append("Date: " + TimeFormat.getDateFormat(log.getDate()) + "\n\n");
							for(String line : log.getLines())
							{
								line = ChatColor.translateAlternateColorCodes('&', line);
								strBuilder.append(line + "\n\n");
							}
							
							bookPages.add(strBuilder.toString());
						}
						bookMeta.setPages(bookPages);
						book.setItemMeta(bookMeta);
						
						player.getInventory().addItem(book);
						
						player.sendMessage(loadedStaffMemberLogbookMessage);
					}
				}.runTask(staffTimesheet);
			}
		}.runTaskAsynchronously(staffTimesheet);
	}
	
	/*
	 * Constructs a String that will be written on the first page of the logbook
	 */
	private String firstPage()
	{
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(ChatColor.BOLD + "       Logbook\n\n" + ChatColor.RESET);
		
		String playerName = new String(staffMember.getPlayerName());
		if(playerName.length() > 16)
		{
			playerName = playerName.substring(0, 16) + "...";
		}
		
		strBuilder.append("Staff Member:" + "\n");
		strBuilder.append(ChatColor.RED + "" + ChatColor.BOLD + playerName + ChatColor.RESET + "\n\n");
		strBuilder.append("Logged Time: " + ChatColor.ITALIC + staffMember.getLoggedTimeString() + "\n\n");
		strBuilder.append(ChatColor.RESET + "Time Goal: " + ChatColor.ITALIC + staffMember.getTimeGoalString() + "\n\n");
		strBuilder.append(ChatColor.RESET + "Progress: " + ChatColor.ITALIC + staffMember.getPercentageTimeCompleted() + "%");
		
		return strBuilder.toString();
	}
		
	public StaffMember getStaffMember()
	{
		return staffMember;
	}
	
	public Calendar getStartDate()
	{
		return startDate;
	}
	
	public Calendar getEndDate()
	{
		return endDate;
	}
}