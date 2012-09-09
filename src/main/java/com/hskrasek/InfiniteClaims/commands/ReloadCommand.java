package com.hskrasek.InfiniteClaims.commands;

import java.util.Date;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import com.hskrasek.InfiniteClaims.InfiniteClaims;

public class ReloadCommand extends IClaimsCommand
{
	InfiniteClaims	plugin;

	public ReloadCommand(InfiniteClaims plugin)
	{
		super(plugin);
		this.plugin = plugin;
		this.setName("Reload Configuration");
		this.setCommandUsage("/iclaims reload");
		this.setArgRange(0, 0);
		this.addKey("iclaimsreload");
		this.addKey("iclaims reload");
		this.addKey("iclaimsr");
		this.addCommandExample("/iclaims reload");
		this.setPermission("iclaims.reload", "Reloads config.yml", PermissionDefault.OP);
	}

	public void runCommand(CommandSender sender, List<String> args)
	{
		sender.sendMessage(plugin.getPluginPrefix() + "Reloading configuration...");

		long startTime = new Date().getTime();

		this.plugin.reloadConfig();

		long endTime = new Date().getTime();

		long difference = endTime - startTime;

		sender.sendMessage(plugin.getPluginPrefix() + "Reload complete!");
		sender.sendMessage(plugin.getPluginPrefix() + "Estimated time: " + ChatColor.YELLOW + difference + "ms");
	}
}
