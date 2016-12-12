package com.antarescraft.kloudy.stafftimesheet.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologuiapi.plugincore.config.ConfigElementMap;
import com.antarescraft.kloudy.hologuiapi.plugincore.config.ConfigProperty;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;

/**
 * Contains all StaffMember objects loaded from config
 * 
 * This class is instantiated populated by the ConfigParser library
 */
public class StaffMembersConfig
{
	private StaffMembersConfig(){}
	
	@ConfigElementMap()
	@ConfigProperty(key = "staff-members", note = "List of staff member configurations")
	private HashMap<UUID, StaffMember> staffMembers;
	
	public StaffMember getStaffMember(String playerName)
	{
		for(StaffMember staffMember : staffMembers.values())
		{
			if(staffMember.getPlayerName().equals(playerName))
			{
				return staffMember;
			}
		}
		
		return null;
	}
	
	public void resetAllStaffMemberTime()
	{
		for(StaffMember staffMember : staffMembers.values())
		{
			staffMember.resetLoggedTime();
		}
	}
	
	public StaffMember getStaffMember(Player player)
	{
		return staffMembers.get(player.getUniqueId());
	}
	
	public Collection<StaffMember> getAllStaffMembers()
	{
		return staffMembers.values();
	}
	
	public void clear()
	{
		staffMembers.clear();
	}
}