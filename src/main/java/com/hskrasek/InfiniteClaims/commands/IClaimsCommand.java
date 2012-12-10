package com.hskrasek.InfiniteClaims.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.hskrasek.InfiniteClaims.InfiniteClaims;
import com.pneumaticraft.commandhandler.Command;

public abstract class IClaimsCommand extends Command
{
	private InfiniteClaims			plugin;

	public IClaimsCommand(InfiniteClaims plugin)
	{
		super(plugin);
		this.plugin = plugin;
	}

	public abstract void runCommand(CommandSender sender, List<String> args);
}
