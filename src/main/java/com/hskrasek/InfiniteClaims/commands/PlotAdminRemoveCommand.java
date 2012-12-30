package com.hskrasek.InfiniteClaims.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.hskrasek.InfiniteClaims.InfiniteClaims;
import com.hskrasek.InfiniteClaims.utils.InfiniteClaimsUtilities;
import com.pneumaticraft.commandhandler.CommandHandler;

public class PlotAdminRemoveCommand extends IClaimsCommand
{
	InfiniteClaims			plugin;
	InfiniteClaimsUtilities	icUtils;

	public PlotAdminRemoveCommand(InfiniteClaims plugin)
	{
		super(plugin);
		this.plugin = plugin;
		this.icUtils = plugin.getIcUtils();
		this.setName("Plot Administration (Remove Plot)");
		this.setCommandUsage(String.format("%s/iclaims admin %sremove {PLOTNAME} %s-p %s{PLAYER} %s-w %s{WORLD}", ChatColor.YELLOW, ChatColor.RED, ChatColor.YELLOW, ChatColor.RED, ChatColor.YELLOW, ChatColor.RED));
		this.setArgRange(2, 5);
		this.addKey("iclaims admin remove");
		this.addKey("iclaimsadminremove");
		this.addKey("icadminrem");
		this.addKey("icadmin remove");
		this.setPermission("iclaims.admin.remove", "Remove another players plot", PermissionDefault.OP);
		this.addCommandExample(String.format("%s/iclaims admin %sremove", ChatColor.YELLOW, ChatColor.RED));
		this.addCommandExample(String.format("%s/iclaims admin %sremove %s-p %sNotch", ChatColor.YELLOW, ChatColor.RED, ChatColor.YELLOW, ChatColor.RED));
	}

	public void runCommand(CommandSender sender, List<String> args)
	{
		Logging.info("args.length = %d", args.size());
		String plotName = args.get(0);
		String playerName = CommandHandler.getFlag("-p", args);
		String worldName = CommandHandler.getFlag("-w", args);

		if (playerName != null)
		{
			if (plotName != null)
			{
				if (worldName != null)
				{
					icUtils.removePlot(sender, playerName, plotName, worldName);
				}
				else
				{
					icUtils.removePlot(sender, playerName, plotName, ((Player) sender).getWorld().getName());
				}
			}
			else
			{
				sender.sendMessage("You must specify the plot you wish to remove.");
			}
		}
		else
		{
			sender.sendMessage("You must specifiy a player");
		}
	}
}
