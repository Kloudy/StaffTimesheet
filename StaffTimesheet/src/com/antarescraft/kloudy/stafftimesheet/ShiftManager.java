package com.antarescraft.kloudy.stafftimesheet;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

import com.antarescraft.kloudy.hologuiapi.plugincore.exceptions.DurationOverflowException;
import com.antarescraft.kloudy.hologuiapi.plugincore.messaging.MessageManager;
import com.antarescraft.kloudy.hologuiapi.plugincore.time.TimeFormat;
import com.antarescraft.kloudy.stafftimesheet.config.BillingPeriod;
import com.antarescraft.kloudy.stafftimesheet.config.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.config.StaffTimesheetConfig;

import org.bukkit.Bukkit;

/**
 * Handles the clocking in and clocking out of staff members.
 */

public class ShiftManager
{
	private static ShiftManager instance;
	
	private BillingPeriod billingPeriod;
	
	private HashMap<UUID, TimeCard> timeCards;//collection containing active staff time cards
		
	public static ShiftManager getInstance()
	{
		if(instance == null)
		{
			instance = new ShiftManager();
		}
		
		return instance;
	}
	
	private ShiftManager()
	{
		timeCards = new HashMap<UUID, TimeCard>();
	}
	
	public void setCurrentBillingPeriod(BillingPeriod billingPeriod)
	{
		this.billingPeriod = billingPeriod;
	}
	
	public BillingPeriod getCurrentBillingPeriod()
	{
		return billingPeriod;
	}
	
	public boolean onTheClock(StaffMember staffMember)
	{
		return timeCards.containsKey(staffMember.getUUID());
	}
	
	public TimeCard getTimeCard(StaffMember staffMember)
	{
		return timeCards.get(staffMember.getUUID());
	}
	
	/*
	 * returns true if the staff member was successfully logged in, false otherwise
	 */
	public boolean clockIn(StaffMember staffMember, String clockInLabel)
	{
		if(!staffMember.getPlayer().hasPermission("staff.shift"))
		{
			MessageManager.error(Bukkit.getConsoleSender(), 
					String.format("Staff member %s must have permission staff.shift to be able to clock in", staffMember.getPlayerName()));
		
			return false;
		}
		
		if(!timeCards.containsKey(staffMember.getUUID()))
		{
			TimeCard timeCard = new TimeCard(staffMember, System.currentTimeMillis());
			timeCards.put(staffMember.getUUID(), timeCard);

			if(staffMember.getClockInCommand() != null)
			{
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), staffMember.getClockInCommand());
			}
			
			staffMember.logEntry(clockInLabel);
		}
		
		return true;
	}
	
	public void clockOut(StaffMember staffMember, String shiftEndReasonLabel)
	{
		if(staffMember != null)
		{
			TimeCard timeCard = timeCards.get(staffMember.getUUID());
			if(timeCard != null)
			{
				long shiftEndTime = System.currentTimeMillis();
				long elapsedMilliseconds = shiftEndTime - timeCard.getStartTime();
							
				Duration shiftTime = Duration.ofMillis(elapsedMilliseconds);
				
				try
				{
					staffMember.addLoggedTime(shiftTime);
					
					billingPeriod.updateStaffMemberSummary(staffMember);
				}
				catch(DurationOverflowException e)//overflowed duration, set staff member logged time to the maximum duration allowed
				{
					staffMember.setLoggedTime(TimeFormat.getMaxDuration());
				}
				
				if(StaffTimesheetConfig.debugMode)
				{
					System.out.println(staffMember.getPlayer().getName() + " shift time: " + TimeFormat.getDurationFormatString(shiftTime));
				}
				
				if(staffMember.getClockOutCommand() != null)
				{
					Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), staffMember.getClockOutCommand());
				}
												
				timeCards.remove(staffMember.getUUID());
				
				staffMember.logEntry(shiftEndReasonLabel);
			}
		}
	}
	
	public void clockOutAll(String shiftEndReasonLabel)
	{
		HashMap<UUID, TimeCard> tempTimeCards = new HashMap<UUID, TimeCard>(timeCards);
		for(TimeCard timeCard : tempTimeCards.values())
		{
			clockOut(timeCard.getStaffMember(), shiftEndReasonLabel);
		}
		
		timeCards.clear();
	}
}