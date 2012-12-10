package com.hskrasek.InfiniteClaims.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.hskrasek.InfiniteClaims.InfiniteClaims;
import com.hskrasek.InfiniteClaims.utils.InfiniteClaimsUtilities;

public class PlotManagementCommand extends IClaimsCommand
{
	InfiniteClaims			plugin;
	InfiniteClaimsUtilities	icUtils;

	public PlotManagementCommand(InfiniteClaims plugin)
	{
		super(plugin);
		this.plugin = plugin;
		this.icUtils = plugin.getIcUtils();
		this.setName("Plot Management");
		this.setCommandUsage(ChatColor.YELLOW + "/iclaims" + ChatColor.RED + " {addmember,removemember,info,reset,remove} ...");
		this.setArgRange(2, 3);
		this.addKey("iclaimsmodify");
		this.addKey("icmodify");
		this.addKey("icm");
		Map<String, Boolean> children = new HashMap<String, Boolean>();
		children.put("iclaims.plot.manage.addmember", true);
		children.put("iclaims.plot.manage.removemember", true);
		children.put("iclaims.plot.manage.info", true);
		children.put("iclaims.plot.manage.reset", true);
		children.put("iclaims.plot.manage.remove", true);
		Permission mod = new Permission("iclaims.plot.manage", "Manage various parts of your plot, see below.", PermissionDefault.OP, children);
		this.setPermission(mod);
		this.addCommandExample(ChatColor.YELLOW + "/iclaims addmember " + ChatColor.RED + "?");
		this.addCommandExample(ChatColor.YELLOW + "/iclaims removemember" + ChatColor.RED + "?");
		this.addCommandExample(ChatColor.YELLOW + "/iclaims info " + ChatColor.RED + "?");
		this.addCommandExample(ChatColor.YELLOW + "/iclaims reset " + ChatColor.RED + "?");
		this.addCommandExample(ChatColor.YELLOW + "/iclaims remove " + ChatColor.RED + "?");
	}

	public void runCommand(CommandSender sender, List<String> args)
	{
		
	}
}
