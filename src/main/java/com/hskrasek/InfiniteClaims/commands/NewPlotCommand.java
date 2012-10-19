package com.hskrasek.InfiniteClaims.commands;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.permissions.PermissionDefault;

import uk.co.jacekk.bukkit.infiniteplots.PlotsGenerator;

import com.hskrasek.InfiniteClaims.InfiniteClaims;
import com.hskrasek.InfiniteClaims.utils.InfiniteClaimsUtilities;

public class NewPlotCommand extends IClaimsCommand
{
	InfiniteClaims			plugin;
	InfiniteClaimsUtilities	icUtils;

	public NewPlotCommand(InfiniteClaims plugin)
	{
		super(plugin);
		this.plugin = plugin;
		this.icUtils = plugin.getIcUtils();
		this.setName("Create a new plot");
		this.setCommandUsage(String.format("%s/iclaims newplot", ChatColor.YELLOW));
		this.setArgRange(0, 2);
		this.addKey("iclaims newplot");
		this.addKey("iclaims getplot");
		this.addKey("icnewplot");
		this.addCommandExample(String.format("%s/iclaims newplot", ChatColor.YELLOW));
		this.setPermission("iclaims.plot.new", "Get a new plot if you have not already reached the maximum", PermissionDefault.OP);
	}

	public void runCommand(CommandSender sender, List<String> args)
	{
		Player player = null;

		if (sender instanceof Player)
		{
			player = (Player) sender;

			ChunkGenerator cg = player.getWorld().getGenerator();
			
			if (cg instanceof PlotsGenerator)
			{
				icUtils.plotAssigner(player.getWorld(), player, plugin.plotHeight, ((PlotsGenerator) cg).getPlotSize(), false);
				return;
			}
			else
			{
				player.sendMessage(plugin.getPluginPrefix() + "You must be in a plot world to get a new plot");
				return;
			}
		}
	}

}
