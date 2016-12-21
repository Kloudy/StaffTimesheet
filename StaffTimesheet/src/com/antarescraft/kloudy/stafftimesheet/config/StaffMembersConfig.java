package com.antarescraft.kloudy.stafftimesheet.config;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.plugincore.config.*;

/**
 * Contains all StaffMember objects loaded from config
 * 
 * This class is instantiated populated by the ConfigParser library
 */
public class StaffMembersConfig
{
	private StaffMembersConfig(){}
	
	@ConfigElementMap
	@ConfigProperty(key = "staff-members", note = "List of staff member configurations")
	private HashMap<String, StaffMember> staffMembers;
	
	public StaffMember getStaffMember(String playerName)
	{
		return staffMembers.get(playerName);
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
		return staffMembers.get(player.getName());
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