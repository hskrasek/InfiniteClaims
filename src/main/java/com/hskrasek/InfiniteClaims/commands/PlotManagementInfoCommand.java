package com.hskrasek.InfiniteClaims.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import com.hskrasek.InfiniteClaims.InfiniteClaims;
import com.pneumaticraft.commandhandler.CommandHandler;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class PlotManagementInfoCommand extends IClaimsCommand
{
	InfiniteClaims	plugin;

	public PlotManagementInfoCommand(InfiniteClaims plugin)
	{
		super(plugin);
		this.plugin = plugin;
		this.setName("Plot Management (Plot Info)");
		this.setCommandUsage(String.format("%s/iclaims info %s{PLOT} -w [WORLD]", ChatColor.YELLOW, ChatColor.RED));
		this.setArgRange(1, 3);
		this.addKey("iclaims info");
		this.addKey("icmi");
		this.addKey("icplot info");
		this.addCommandExample(String.format("%s/iclaims info %splot1", ChatColor.YELLOW, ChatColor.RED));
		this.addCommandExample(String.format("%s/iclaims info %splot1 %s-w %sCreative", ChatColor.YELLOW, ChatColor.RED, ChatColor.YELLOW, ChatColor.RED));
		this.setPermission("iclaims.plot.manage.info", "Get information about your plots", PermissionDefault.OP);
	}

	public void runCommand(CommandSender sender, List<String> args)
	{
		String plotName = args.get(0);
		String worldName = CommandHandler.getFlag("-w", args);

		Player player = null;

		if (sender instanceof Player)
		{
			player = (Player) sender;
			WorldGuardPlugin wgp = plugin.getWorldGuard();
			player.sendMessage(String.format("%sName: %s%s", ChatColor.YELLOW, ChatColor.RED, plotName));

			if (worldName != null)
			{
				RegionManager mgr = wgp.getRegionManager(plugin.getServer().getWorld(worldName));

				String members = "";

				try
				{
					ProtectedRegion plot = mgr.getRegion(player.getName() + plotName);
					members = plot.getMembers().toPlayersString();
				}
				catch (NullPointerException npe)
				{
					player.sendMessage(String.format("%s%s %sis not a valid plot world! Please try again!", ChatColor.RED, worldName, ChatColor.YELLOW));
				}
				player.sendMessage(String.format("%sMembers: %s%s", ChatColor.YELLOW, ChatColor.RED, members));
			}
			else
			{
				RegionManager mgr = wgp.getRegionManager(player.getWorld());

				ProtectedRegion plot = mgr.getRegion(player.getName() + plotName);
				String members = plot.getMembers().toPlayersString();
				player.sendMessage(String.format("%sMembers: %s%s", ChatColor.YELLOW, ChatColor.RED, members));
			}
		}
	}
}
