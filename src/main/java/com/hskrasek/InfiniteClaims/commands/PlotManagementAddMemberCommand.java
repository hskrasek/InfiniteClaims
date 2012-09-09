package com.hskrasek.InfiniteClaims.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import com.hskrasek.InfiniteClaims.InfiniteClaims;
import com.hskrasek.InfiniteClaims.utils.InfiniteClaimsUtilities;
import com.pneumaticraft.commandhandler.CommandHandler;

public class PlotManagementAddMemberCommand extends IClaimsCommand
{
	InfiniteClaims			plugin;
	InfiniteClaimsUtilities	icUtils;

	public PlotManagementAddMemberCommand(InfiniteClaims plugin)
	{
		super(plugin);
		this.plugin = plugin;
		this.icUtils = plugin.getIcUtils();
		this.setName("Plot Management (Add a member)");
		this.setCommandUsage(String.format("%s/iclaims addmember %s{PLOT} {PLAYER} %s-w %s[WORLD]", ChatColor.YELLOW, ChatColor.RED, ChatColor.WHITE, ChatColor.RED));
		this.setArgRange(2, 4);
		this.addKey("iclaimsmodify add");
		this.addKey("icmodify addmember");
		this.addKey("iclaims addmember");
		this.addKey("icma");
		this.addCommandExample(String.format("%s/iclaims addmember %splot1 Notch", ChatColor.YELLOW, ChatColor.RED));
		this.addCommandExample(String.format("%s/iclaims addmember %splot1 CoolGuy %s-w %sCompetitionWorld", ChatColor.YELLOW, ChatColor.RED, ChatColor.WHITE, ChatColor.RED));
		this.setPermission("iclaims.plot.manage.addmember", "Add a member to your plot. If you don't provide a world, your current one will be used.", PermissionDefault.OP);
	}

	public void runCommand(CommandSender sender, List<String> args)
	{
		String plotName = args.get(0);
		String memberToAdd = args.get(1);
		String worldName = CommandHandler.getFlag("-w", args);

		Player player = null;

		if (sender instanceof Player)
		{
			player = (Player) sender;

			if (worldName != null)
			{
				icUtils.addMember(player.getName(), memberToAdd, plotName, plugin.getServer().getWorld(worldName));
				return;
			}
			else
			{
				icUtils.addMember(player.getName(), memberToAdd, plotName, player.getWorld());
			}
		}
	}
}
