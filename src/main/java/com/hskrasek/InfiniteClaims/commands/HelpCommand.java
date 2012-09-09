package com.hskrasek.InfiniteClaims.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;

import com.hskrasek.InfiniteClaims.InfiniteClaims;
import com.pneumaticraft.commandhandler.Command;

public class HelpCommand extends PaginatedCoreCommand<Command>
{
	InfiniteClaims	plugin;

	public HelpCommand(InfiniteClaims plugin)
	{
		super(plugin);
		this.plugin = plugin;
		this.setName("IClaims Help");
		this.setCommandUsage("/iclaims");
		this.setArgRange(0, 1);
		this.addKey("iclaims");
		this.addKey("iclaims help");
		this.addCommandExample("/iclaims help");
		this.setPermission("iclaims.help", "Displays help menu", PermissionDefault.TRUE);
	}

	public void runCommand(CommandSender sender, List<String> args)
	{
		sender.sendMessage(ChatColor.YELLOW + "=== InfiniteClaims Help ===");
		List<Command> availableCommands = new ArrayList<Command>(this.plugin.getCommandHandler().getCommands(sender));

		for (Command c : availableCommands)
		{
			sender.sendMessage(ChatColor.YELLOW + c.getCommandUsage());
		}

		sender.sendMessage(ChatColor.YELLOW + "Add a '" + ChatColor.RED + "?" + ChatColor.YELLOW + "' after a command to learn more");
	}

	protected List<Command> getFilteredItems(List<Command> availableItems, String filter)
	{
		List<Command> filtered = new ArrayList<Command>();

		for (Command c : availableItems)
		{
			if (stitchThisString(c.getKeyStrings()).matches("(?i).*" + filter + ".*"))
			{
				filtered.add(c);
			}
			else if (c.getCommandName().matches("(?i).*" + filter + ".*"))
			{
				filtered.add(c);
			}
			else if (c.getCommandDesc().matches("(?i).*" + filter + ".*"))
			{
				filtered.add(c);
			}
			else if (c.getCommandUsage().matches("(?i).*" + filter + ".*"))
			{
				filtered.add(c);
			}
			else
			{
				for (String example : c.getCommandExamples())
				{
					if (example.matches("(?i).*" + filter + ".*"))
					{
						filtered.add(c);
						break;
					}
				}
			}
		}
		return filtered;
	}

	protected String getItemText(Command item)
	{
		return null;
	}
}
