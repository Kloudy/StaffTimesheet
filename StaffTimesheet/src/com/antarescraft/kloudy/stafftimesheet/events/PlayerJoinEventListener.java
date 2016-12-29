package com.antarescraft.kloudy.stafftimesheet.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.antarescraft.kloudy.stafftimesheet.ShiftManager;
import com.antarescraft.kloudy.stafftimesheet.StaffTimesheet;
import com.antarescraft.kloudy.stafftimesheet.config.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.config.StaffTimesheetConfig;

public class PlayerJoinEventListener implements Listener
{
	private StaffTimesheet staffTimesheet;
	
	public PlayerJoinEventListener(StaffTimesheet staffTimesheet)
	{
		this.staffTimesheet = staffTimesheet;
	}
	
	@EventHandler
	public void playerJoinEvent(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		
		StaffTimesheetConfig config = StaffTimesheetConfig.getConfig(staffTimesheet);
		
		StaffMember staffMember = config.getStaffMembersConfig().getStaffMember(player);
		if(staffMember != null && staffMember.startShiftOnLogin())
		{
			ShiftManager.getInstance().clockIn(staffMember, config.getEventLabelConfig().getShiftStart());
			
			staffMember.getPlayer().sendMessage(config.getShiftStartStopMessagesConfig().getShiftStart(staffMember));
		}
	}
}