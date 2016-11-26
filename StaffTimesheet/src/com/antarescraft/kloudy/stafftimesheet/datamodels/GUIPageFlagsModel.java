package com.antarescraft.kloudy.stafftimesheet.datamodels;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.hologuiapi.HoloGUIPlugin;
import com.antarescraft.kloudy.hologuiapi.guicomponents.GUIPage;
import com.antarescraft.kloudy.hologuiapi.playerguicomponents.PlayerGUIPageModel;

public class GUIPageFlagsModel extends PlayerGUIPageModel
{

	public GUIPageFlagsModel(HoloGUIPlugin plugin, GUIPage guiPage, Player player)
	{
		super(plugin, guiPage, player);
	}
}