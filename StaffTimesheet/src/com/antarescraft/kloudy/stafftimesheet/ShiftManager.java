package com.antarescraft.kloudy.stafftimesheet;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

import com.antarescraft.kloudy.hologuiapi.plugincore.exceptions.DurationOverflowException;
import com.antarescraft.kloudy.hologuiapi.plugincore.time.TimeFormat;
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
	
	public void clockIn(StaffMember staffMember, String clockInLabel)
	{
		if(!timeCards.containsKey(staffMember.getUUID()))
		{
			TimeCard timeCard = new TimeCard(staffMember, System.currentTimeMillis());
			timeCards.put(staffMember.getUUID(), timeCard);
			
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), String.format("pex user %s add %s", 
					staffMember.getPlayer().getName(), staffMember.getClockInPermission()));
			
			staffMember.logEntry(clockInLabel);
		}
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
				
				if(StaffTimesheet.debugMode)
				{
					System.out.println(staffMember.getPlayer().getName() + " shift time: " + TimeFormat.getDurationFormatString(shiftTime));
				}
				
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), String.format("pex user %s remove %s",
						staffMember.getPlayer().getName(), staffMember.getClockInPermission()));
												
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