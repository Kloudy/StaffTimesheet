package com.antarescraft.kloudy.stafftimesheet.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.antarescraft.kloudy.hologui.events.HoloGUITextBoxUpdateEvent;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;
import com.antarescraft.kloudy.stafftimesheet.StaffMemberAdmin;
import com.antarescraft.kloudy.stafftimesheet.util.ConfigManager;

public class HoloGUITextBoxUpdateEventListener implements Listener
{
	private ConfigManager configManager;
	
	public HoloGUITextBoxUpdateEventListener(ConfigManager configManager)
	{
		this.configManager = configManager;
	}
	
	@EventHandler
	public void holoGUITextboxUpdateEvent(HoloGUITextBoxUpdateEvent event)
	{
		Player player = event.getPlayer();
		StaffMember staffMember = configManager.getStaffMember(player);
		if(staffMember instanceof StaffMemberAdmin)
		{
			StaffMemberAdmin admin = (StaffMemberAdmin)staffMember;
			admin.setSearchedStaffMember(event.getTextBox().getPlayerTextBoxValue(player));
		}
	}
}