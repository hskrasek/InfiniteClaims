package com.hskrasek.InfiniteClaims.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import com.hskrasek.InfiniteClaims.InfiniteClaims;
import com.hskrasek.InfiniteClaims.utils.InfiniteClaimsUtilities;
import com.pneumaticraft.commandhandler.CommandHandler;

public class PlotManagementRemoveMemberCommand extends IClaimsCommand
{
	InfiniteClaims			plugin;
	InfiniteClaimsUtilities	icUtils;

	public PlotManagementRemoveMemberCommand(InfiniteClaims plugin)
	{
		super(plugin);
		this.plugin = plugin;
		this.icUtils = plugin.getIcUtils();
		this.setName("Plot Management (Remove a member)");
		this.setCommandUsage(String.format("%s/iclaims removemember %s{PLOT} {PLAYER}[all] %s-w %s[WORLD]", ChatColor.YELLOW, ChatColor.RED, ChatColor.WHITE, ChatColor.RED));
		this.setArgRange(2, 4);
		this.addKey("iclaimsmodify remove");
		this.addKey("icmodify removemember");
		this.addKey("iclaims removemember");
		this.addKey("icmr");
		this.addCommandExample(String.format("%s/iclaims removemember %splot1 Notch", ChatColor.YELLOW, ChatColor.RED));
		this.addCommandExample(String.format("%s/iclaims removemember %splot1 all", ChatColor.YELLOW, ChatColor.RED));
		this.addCommandExample(String.format("%s/iclaims removemember %splot1 all %s-w %s CompetitionWorld", ChatColor.YELLOW, ChatColor.RED, ChatColor.WHITE, ChatColor.RED));
		this.addCommandExample(String.format("%s/iclaims removemember %splot1 CoolGuy %s-w %sCompetitionWorld", ChatColor.YELLOW, ChatColor.RED, ChatColor.WHITE, ChatColor.RED));
		this.setPermission("iclaims.plot.manage.removemember", "Remove a member from your plot. If you don't provide a world, your current one will be used.", PermissionDefault.OP);
	}

	public void runCommand(CommandSender sender, List<String> args)
	{
		String plotName = args.get(0);
		String memberToRemove = args.get(1);
		String worldName = CommandHandler.getFlag("-w", args);

		if (plotName == null)
		{
			sender.sendMessage(String.format("%s%sYou must specify a plot name to remove a member from.", plugin.getPluginPrefix(), ChatColor.RED));
			return;
		}

		if (memberToRemove == null)
		{
			sender.sendMessage(String.format("%s%SYou must specifiy a member to remove. ", plugin.getPluginPrefix(), ChatColor.RED));
			return;
		}

		Player player = null;

		if (sender instanceof Player)
		{
			player = (Player) sender;

			if (worldName != null)
			{
				if (memberToRemove.equalsIgnoreCase("all"))
				{
					icUtils.removeMember(player.getName(), "all", plotName, plugin.getServer().getWorld(worldName));
				}
				else
				{
					icUtils.removeMember(player.getName(), memberToRemove, plotName, plugin.getServer().getWorld(worldName));
				}
			}
			else
			{
				if (memberToRemove.equalsIgnoreCase("all"))
				{
					icUtils.removeMember(player.getName(), "all", plotName, player.getWorld());
				}
				else
				{
					icUtils.removeMember(player.getName(), memberToRemove, plotName, player.getWorld());
				}
			}
		}
	}

}
