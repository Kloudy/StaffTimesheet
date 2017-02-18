package com.antarescraft.kloudy.stafftimesheet.config;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologuiapi.plugincore.config.*;
import com.antarescraft.kloudy.hologuiapi.plugincore.config.annotations.*;

/**
 * Contains all StaffMember objects loaded from config
 * 
 * This class is instantiated populated by the ConfigParser library
 */
public class StaffMembersConfig implements ConfigObject
{
	private StaffMembersConfig(){}
	
	@OptionalConfigProperty
	@ConfigElementMap
	@ConfigProperty(key = "staff-members")
	private HashMap<String, StaffMember> staffMembers = new HashMap<String, StaffMember>();
	
	public void resetAllStaffMemberTime()
	{
		for(StaffMember staffMember : staffMembers.values())
		{
			staffMember.resetLoggedTime();
		}
	}
	
	public StaffMember getStaffMember(String playerName)
	{
		return staffMembers.get(playerName);
	}
	
	public StaffMember getStaffMember(Player player)
	{
		for(StaffMember staffMember : staffMembers.values())
		{
			if(staffMember.getUUID().equals(player.getUniqueId()))
			{
				return staffMember;
			}
		}
		
		return null;
	}
	
	public Collection<StaffMember> getAllStaffMembers()
	{
		return staffMembers.values();
	}
	
	public void clear()
	{
		staffMembers.clear();
	}

	@Override
	public void configParseComplete(PassthroughParams params){}
}