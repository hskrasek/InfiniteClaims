package com.hskrasek.InfiniteClaims.commands;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.hskrasek.InfiniteClaims.InfiniteClaims;
import com.hskrasek.InfiniteClaims.utils.InfiniteClaimsUtilities;
import com.pneumaticraft.commandhandler.CommandHandler;

public class PlotTeleportCommand extends IClaimsCommand
{
	InfiniteClaims			plugin;
	InfiniteClaimsUtilities	icUtils;

	public PlotTeleportCommand(InfiniteClaims plugin)
	{
		super(plugin);
		this.plugin = plugin;
		this.icUtils = plugin.getIcUtils();
		this.setName("Plot Teleportation");
		this.setCommandUsage(String.format("%s/iclaims plot %s{PLOT} %s-p %s[PLAYER]%s -w %s[WORLD]", ChatColor.YELLOW, ChatColor.RED, ChatColor.YELLOW, ChatColor.WHITE, ChatColor.YELLOW, ChatColor.WHITE));
		this.setArgRange(1, 5);
		this.addKey("icplot");
		this.addKey("iclaimsplot");
		this.addKey("iclaims plot");
		Permission perm = new Permission("iclaims.plot.tp", "InfiniteClaims Plot Teleportation", PermissionDefault.OP);
		this.setPermission(perm);
		this.addCommandExample(ChatColor.YELLOW + "/iclaims plot " + ChatColor.RED + "plot1");
		this.addCommandExample(ChatColor.YELLOW + "/iclaims plot " + ChatColor.RED + "plot1 " + ChatColor.YELLOW + "-p " + ChatColor.WHITE + "HeroSteve");
		this.addCommandExample(ChatColor.YELLOW + "/iclaims plot " + ChatColor.RED + "plot1 " + ChatColor.YELLOW + "-w " + ChatColor.WHITE + "DonatorCreative");
		this.addCommandExample(ChatColor.YELLOW + "/iclaims plot " + ChatColor.RED + "plot1 " + ChatColor.YELLOW + "-p " + ChatColor.WHITE + "HeroSteve" + ChatColor.YELLOW + "-w " + ChatColor.WHITE + "DonatorCreative");
	}

	public void runCommand(CommandSender sender, List<String> args)
	{
		String plotName = args.get(0);
		String playerName = CommandHandler.getFlag("-p", args);
		String worldName = CommandHandler.getFlag("-w", args);

		Player player = null;

		if (sender instanceof Player)
		{
			player = (Player) sender;

			// Assuming the player specified a user
			if (playerName != null)
			{
				playerName = playerName.toLowerCase();
				// Assuming the player specified a world
				if (worldName != null)
				{
					// Teleport to the specified players plot in the specified
					// world
					icUtils.teleportToOtherPlot(player, playerName, plotName, worldName);
				}
				else
				{
					// Teleport to the specified players plot, in the current
					// world
					worldName = player.getLocation().getWorld().getName();
					icUtils.teleportToOtherPlot(player, playerName, plotName, worldName);
				}
			}
			else
			{
				if (worldName != null)
				{
					// Teleport to players plot in specified world
					icUtils.teleportToPlot(player, plotName, worldName);
				}
				else
				{
					// Finally, just teleport the player to their own plot in
					// the world they are currently in
					worldName = player.getLocation().getWorld().getName();
					icUtils.teleportToPlot(player, plotName, worldName);
				}
			}
		}
	}

}
