package com.antarescraft.kloudy.stafftimesheet.datamodels;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologui.HoloGUIPlugin;
import com.antarescraft.kloudy.hologui.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologui.playerguicomponents.PlayerGUIPageModel;
import com.antarescraft.kloudy.stafftimesheet.StaffMember;

public class ManageTimePageModel extends PlayerGUIPageModel
{
	private StaffMember staffMember;

	public ManageTimePageModel(HoloGUIPlugin plugin, GUIPage guiPage, Player player, StaffMember staffMember) 
	{
		super(plugin, guiPage, player);
		
		this.staffMember = staffMember;
	}
	
	public String getStaffMemberName()
	{
		return staffMember.getPlayerName();
	}
	
	public String getStaffMemberLoggedTime()
	{
		return staffMember.getLoggedTimeString();
	}
	
	public String getStaffMemberTimeGoal()
	{
		return staffMember.getTimeGoal();
	}
}