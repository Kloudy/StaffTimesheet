package com.antarescraft.kloudy.stafftimesheet;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;

import com.antarescraft.kloudy.stafftimesheet.util.TimeFormat;

/**
 * Shift Manager
 *
 * Handles the clocking in and clocking out of staff members.
 * 
 */
public class ShiftManager
{
	private static ShiftManager instance;
	
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
	
	public boolean onTheClock(StaffMember staffMember)
	{
		return timeCards.containsKey(staffMember.getUUID());
	}
	
	public TimeCard getTimeCard(StaffMember staffMember)
	{
		return timeCards.get(staffMember.getUUID());
	}
	
	public void clockIn(StaffMember staffMember)
	{
		if(!timeCards.containsKey(staffMember.getUUID()))
		{
			TimeCard timeCard = new TimeCard(staffMember, System.currentTimeMillis());
			timeCards.put(staffMember.getUUID(), timeCard);
			
			Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), String.format("pex user %s add %s", 
					staffMember.getPlayer().getName(), staffMember.getClockInPermission()));
		}
	}
	
	public void clockOut(StaffMember staffMember, ShiftEndReason shiftEndedReason)
	{
		if(staffMember != null)
		{
			TimeCard timeCard = timeCards.get(staffMember.getUUID());
			if(timeCard != null)
			{
				long shiftEndTime = System.currentTimeMillis();
				long elapsedMilliseconds = shiftEndTime - timeCard.getStartTime();
							
				Duration shiftTime = Duration.ofMillis(elapsedMilliseconds);
				staffMember.addLoggedTime(shiftTime);
				
				if(StaffTimesheet.debugMode)
				{
					System.out.println(staffMember.getPlayer().getName() + " shift time: " + TimeFormat.getTimeFormat(shiftTime));
				}
				
				Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), String.format("pex user %s remove %s",
						staffMember.getPlayer().getName(), staffMember.getClockInPermission()));
												
				timeCards.remove(staffMember.getUUID());
			}
		}
	}
	
	public void clockOutAll(ShiftEndReason shiftEndedReason)
	{
		HashMap<UUID, TimeCard> tempTimeCards = new HashMap<UUID, TimeCard>(timeCards);
		for(TimeCard timeCard : tempTimeCards.values())
		{
			clockOut(timeCard.getStaffMember(), shiftEndedReason);
		}
		
		timeCards.clear();
	}
}