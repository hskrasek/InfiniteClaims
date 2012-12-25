package com.hskrasek.InfiniteClaims.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import com.hskrasek.InfiniteClaims.InfiniteClaims;
import com.hskrasek.InfiniteClaims.exceptions.PropertyNotFoundException;

public class PlotAdminConfigurationCommand extends IClaimsCommand
{
	InfiniteClaims	plugin;

	public PlotAdminConfigurationCommand(InfiniteClaims plugin)
	{
		super(plugin);
		this.plugin = plugin;
		this.setName("Plot Administration (Configuration Command)");
		this.setCommandUsage(ChatColor.YELLOW + "/iclaims admin " + ChatColor.RED + "conf" + ChatColor.YELLOW + " {PROPERTY} {VALUE}");
		this.setArgRange(1, 2);
		this.addKey("iclaims admin conf");
		this.addKey("icadmin conf");
		this.addKey("icac");
		this.setPermission("iclaims.admin.config", "Admin info command. Used to get any info you would need from InfiniteClaims", PermissionDefault.OP);
		this.addCommandExample(ChatColor.YELLOW + "/iclaims admin " + ChatColor.RED + "conf");
		this.addCommandExample(ChatColor.YELLOW + "/iclaims admin " + ChatColor.RED + "conf signs.prefix-color 3");
		this.addCommandExample(ChatColor.YELLOW + "/iclaims admin " + ChatColor.RED + "conf signs.enabled false");
	}

	public void runCommand(CommandSender sender, List<String> args)
	{
		if(args.size() <= 1)
		{
			StringBuilder builder = new StringBuilder();
			for(String configKey : plugin.config.getKeys())
			{
				if(plugin.config.get(configKey) == null)
				{
					continue;
				}
				builder.append(ChatColor.YELLOW);
				builder.append(configKey).append(" ").append(ChatColor.WHITE).append("=").append(" ");
				builder.append(ChatColor.RED).append(plugin.config.get(configKey));
				builder.append(ChatColor.WHITE).append(", ");
			}
			
			String configMessage = builder.toString();
			configMessage = configMessage.substring(0, configMessage.length() - 2);
			sender.sendMessage(configMessage);
			return;
		}
		
		if(args.get(0).toLowerCase().contains("height"))
		{
			sender.sendMessage(String.format("%sIt is not recommended to change height while in game", ChatColor.RED));
		}
		
		try
		{
			plugin.config.set(args.get(0).toLowerCase(), args.get(1));
			plugin.reloadConfig();
			sender.sendMessage(String.format("%sValue set! Configuration reloaded.", ChatColor.GREEN));
		}
		catch(PropertyNotFoundException e)
		{
			sender.sendMessage(String.format("%sSetting '%s' to '%s' could not be done, %s is not a valid property.", ChatColor.RED, args.get(0).toLowerCase(), args.get(1), args.get(0).toLowerCase()));
		}
	}
}
