package com.hskrasek.InfiniteClaims.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import com.hskrasek.InfiniteClaims.InfiniteClaims;
import com.hskrasek.InfiniteClaims.exceptions.InvalidWorldException;
import com.hskrasek.InfiniteClaims.utils.InfiniteClaimsUtilities;

public class PlotManagementResetCommand extends IClaimsCommand
{

	InfiniteClaims			plugin;
	InfiniteClaimsUtilities	icUtils;

	public PlotManagementResetCommand(InfiniteClaims plugin)
	{
		super(plugin);
		this.plugin = plugin;
		this.icUtils = plugin.getIcUtils();
		this.setName("Plot Reset Command");
		this.setCommandUsage("/iclaims reset {PLOTNAME}");
		this.setArgRange(1, 2);
		this.addKey("iclaims reset");
		this.addKey("iclaimsr");
		this.setPermission("iclaims.plot.reset", "Reset plot to a clean slate", PermissionDefault.OP);
		this.addCommandExample("/iclaims reset plot1");
		this.addCommandExample("/iclaims reset plot2 -w Creative");
	}

	public void runCommand(CommandSender sender, List<String> args)
	{
		// String worldName = CommandHandler.getFlag("-w", args);
		String plotName = args.get(0);

		Player player = null;
		if (sender instanceof Player)
		{
			player = (Player) sender;
		}

		if (plotName != null)
		{
			try
			{
				icUtils.regeneratePlot(player.getName(), plotName, player.getWorld().getName());
			}
			catch (InvalidWorldException e)
			{
				sender.sendMessage(plugin.pluginPrefix + plugin.messages.getMessage("not-plotworld-error", player.getWorld().getName()));
				e.printStackTrace();
			}
		}
		else
		{
			sender.sendMessage("Please provide a plotname to reset");
		}
	}
}
