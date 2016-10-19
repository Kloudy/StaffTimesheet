package com.antarescraft.kloudy.stafftimesheet.events;

/*public class HoloGUITextBoxUpdateEventListener implements Listener
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
		if(staffMember.isSuperAdmin())
		{
			StaffMemberAdmin admin = (StaffMemberAdmin)staffMember;
			admin.setSearchedStaffMember(event.getTextBox().getPlayerTextBoxValue(player));
		}
	}
}*/