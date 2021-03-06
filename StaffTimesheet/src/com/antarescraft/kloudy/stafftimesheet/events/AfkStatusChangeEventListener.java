package com.antarescraft.kloudy.stafftimesheet.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.antarescraft.kloudy.stafftimesheet.ShiftManager;
import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;
import com.antarescraft.kloudy.stafftimesheet.config.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.config.StaffTimesheetConfig;
import com.earth2me.essentials.UserData;

import net.ess3.api.events.AfkStatusChangeEvent;

public class AfkStatusChangeEventListener implements Listener
{
	private StaffTimesheet staffTimesheet;
	
	public AfkStatusChangeEventListener(StaffTimesheet staffTimesheet)
	{
		this.staffTimesheet = staffTimesheet;
	}
	
	@EventHandler
	public void afkStatusChangeEvent(AfkStatusChangeEvent event)
	{
		UserData user = (UserData)event.getAffected();
		
		Player player = user.getBase();
		
		StaffTimesheetConfig config = StaffTimesheetConfig.getConfig(staffTimesheet);
		
		StaffMember staffMember = config.getStaffMembersConfig().getStaffMember(player);
		ShiftManager shiftManager = ShiftManager.getInstance();
		
		if(staffMember != null)
		{
			if(event.getValue())
			{
				
				if(shiftManager.onTheClock(staffMember))
				{
					shiftManager.clockOut(staffMember, config.getEventLabelConfig().getShiftEndAfk());
				
					player.sendMessage(config.getShiftStartStopMessagesConfig().getShiftEndAfk(staffMember));
				}
			}
			else
			{
				if(!shiftManager.onTheClock(staffMember))
				{
					boolean success = shiftManager.clockIn(staffMember, config.getEventLabelConfig().getShiftStartReturnFromAfk());
					if(success)player.sendMessage(config.getShiftStartStopMessagesConfig().getShiftStartReturnFromAfk(staffMember));
				}
			}
		}
	}
}