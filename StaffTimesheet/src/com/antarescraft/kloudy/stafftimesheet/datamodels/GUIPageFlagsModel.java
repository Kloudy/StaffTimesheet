package com.antarescraft.kloudy.stafftimesheet.datamodels;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologui.HoloGUIPlugin;
import com.antarescraft.kloudy.hologui.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologui.playerguicomponents.PlayerGUIPageModel;

public class GUIPageFlagsModel extends PlayerGUIPageModel
{

	public GUIPageFlagsModel(HoloGUIPlugin plugin, GUIPage guiPage, Player player)
	{
		super(plugin, guiPage, player);
	}
}