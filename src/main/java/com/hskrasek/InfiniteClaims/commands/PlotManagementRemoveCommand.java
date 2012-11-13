package com.hskrasek.InfiniteClaims.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import com.hskrasek.InfiniteClaims.InfiniteClaims;
import com.hskrasek.InfiniteClaims.utils.InfiniteClaimsUtilities;
import com.pneumaticraft.commandhandler.CommandHandler;

public class PlotManagementRemoveCommand extends IClaimsCommand
{
	InfiniteClaims			plugin;
	InfiniteClaimsUtilities	icUtils;
	
	public PlotManagementRemoveCommand(InfiniteClaims plugin)
	{
		super(plugin);
		this.plugin = plugin;
		this.icUtils = plugin.getIcUtils();
		this.setName("Plot Management (Remove Plot)");
		this.setCommandUsage(String.format("%s/iclaims remove %s{PLOTNAME} %s-w %s{WORLD}", ChatColor.YELLOW, ChatColor.RED, ChatColor.YELLOW, ChatColor.RED));
		this.setArgRange(1, 3);
		this.addKey("iclaims remove");
		this.addKey("iclaimsremove");
		this.addKey("icplot remove");
		this.addKey("icpr");
		this.setPermission("iclaims.plot.manage.remove", "Remove an un-needed plot.", PermissionDefault.OP);
		this.addCommandExample(String.format("%s/icplot remove %splot1", ChatColor.YELLOW, ChatColor.RED));
		this.addCommandExample(String.format("%s/iclaims remove %splot1 %s-w %sCreative", ChatColor.YELLOW, ChatColor.RED, ChatColor.YELLOW, ChatColor.RED));
	}

	public void runCommand(CommandSender sender, List<String> args)
	{
		String plotName = args.get(0);
		String worldName = CommandHandler.getFlag("-w", args);

		if (plotName != null)
		{
			if (worldName != null)
			{
				icUtils.removePlot(sender, sender.getName(), plotName, worldName);
			}
			else
			{
				icUtils.removePlot(sender, sender.getName(), plotName, ((Player) sender).getWorld().getName());
			}
		}
		else
		{
			sender.sendMessage("You must specify the plot you wish to remove.");
		}
	}

}
