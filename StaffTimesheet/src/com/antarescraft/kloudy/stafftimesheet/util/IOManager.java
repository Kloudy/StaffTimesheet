package com.antarescraft.kloudy.stafftimesheet.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.commons.io.IOUtils;
import org.bukkit.Bukkit;

import com.antarescraft.kloudy.plugincore.messaging.MessageManager;
import com.antarescraft.kloudy.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;

public class IOManager 
{	
	public static void initFileStructure(StaffTimesheet staffTimesheet)
	{
		try
		{
			File folder = new File("plugins/" + StaffTimesheet.pluginName);
			if(!folder.exists())//plugin data folder
			{
				folder.mkdir();
			}
			
			//copy the staff-members.yml file into the plugin data folder
			InputStream inputStream = staffTimesheet.getResource("staff-members.yml");
			File staffMembersYmlFile = new File(String.format("plugins/%s/staff-members.yml", staffTimesheet.getName()));
			if(!staffMembersYmlFile.exists())
			{
				FileOutputStream output = new FileOutputStream(staffMembersYmlFile);
				output.write(IOUtils.toByteArray(inputStream));
				
				inputStream.close();
				output.close();
			}
			
			folder = new File("plugins/" + StaffTimesheet.pluginName + "/staff_logs");
			if(!folder.exists())//staff logs
			{
				folder.mkdir();
			}
		}
		catch(Exception e){}		
	}
	
	public static void saveLogEntry(StaffMember staffMember, String text)
	{	
		String timeFormat = TimeFormat.generateTimestamp("yyyy-MM-dd");
		
		File staffMemberFolder = new File(String.format("plugins/%s/staff_logs/%s", 
				StaffTimesheet.pluginName, staffMember.getPlayerName()));
		
		if(!staffMemberFolder.exists())
		{
			staffMemberFolder.mkdir();
		}
		
		File logFile = new File(String.format("plugins/%s/staff_logs/%s/%s.txt", 
				StaffTimesheet.pluginName, staffMember.getPlayerName(), timeFormat));
		if(!logFile.exists())
		{
			try
			{
				logFile.createNewFile();
			} 
			catch (IOException e)
			{
				if(StaffTimesheet.debugMode) e.printStackTrace();
				
				error(staffMember);
			}
		}
		
		PrintWriter out = null;
		try
		{
			/*Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logFile), "UTF-8"));
			try 
			{
			    out.write(text);
			} 
			finally
			{
			    out.close();
			}*/
			FileWriter fileWriter = new FileWriter(logFile, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			out = new PrintWriter(bufferedWriter);
			
			text = text.replace("§", "&");
			out.println(text);
			
			out.close();
		} 
		catch (IOException e)
		{
			if(StaffTimesheet.debugMode) e.printStackTrace();
			
			error(staffMember);
		}
	}
	
	/**
	 * 
	 * @param staffTimesheet
	 * @param staffMember
	 * @return an ArrayList of the lines in the log file for the specified staff member
	 */
	public static ArrayList<String> getLogFile(StaffMember staffMember, Calendar date)
	{
		File logFile = new File(String.format("plugins/%s/staff_logs/%s/%s.txt", 
				StaffTimesheet.pluginName, staffMember.getPlayerName(), TimeFormat.getDateFormat(date).replace("/", "-")));
		
		if(logFile.exists())
		{ 
			try 
			{
				return (ArrayList<String>)Files.readAllLines(logFile.toPath());
			} 
			catch (IOException e)
			{
				if(StaffTimesheet.debugMode) e.printStackTrace();
			}
		}
		
		return null;
	}
	
	private static void error(StaffMember staffMember)
	{
		MessageManager.error(Bukkit.getConsoleSender(), 
				String.format("[%s] An error occured while attempting to create a log entry for staff member %s", 
						StaffTimesheet.pluginName, staffMember.getPlayerName()));
	}
}