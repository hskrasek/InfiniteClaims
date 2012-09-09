package com.hskrasek.InfiniteClaims.commands;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.ConversationPrefix;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.permissions.PermissionDefault;

import com.hskrasek.InfiniteClaims.InfiniteClaims;
import com.pneumaticraft.commandhandler.CommandHandler;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;


public class PlotAdminInfoCommand extends IClaimsCommand
{
	InfiniteClaims	plugin;

	public PlotAdminInfoCommand(InfiniteClaims plugin)
	{
		super(plugin);
		this.plugin = plugin;
		this.setName("Plot Administration (Info Command)");
		this.setCommandUsage(ChatColor.YELLOW + "/iclaims admin " + ChatColor.RED + "info [WORLDS|PLOT]" + ChatColor.YELLOW + "-w [WORLD]");
		this.setArgRange(1, 4);
		this.addKey("iclaims admin info");
		this.addKey("icadmin info");
		this.addKey("icai");
		this.setPermission("iclaims.admin.info", "Admin info command. Used to get any info you would need from InfiniteClaims", PermissionDefault.OP);
		this.addCommandExample(ChatColor.YELLOW + "/iclaims admin " + ChatColor.RED + "info");
		this.addCommandExample(ChatColor.YELLOW + "/iclaims admin " + ChatColor.RED + "info worlds");
		this.addCommandExample(ChatColor.YELLOW + "/iclaims admin " + ChatColor.RED + "info plot");
	}

	public void runCommand(CommandSender sender, List<String> args)
	{
		String option = args.get(0);
		String worldName = CommandHandler.getFlag("-w", args);

		if (option.equalsIgnoreCase("worlds"))
		{
			if (worldName != null)
			{
				int total = plugin.getIcUtils().getNumberPlotsForWorld(plugin.getServer().getWorld(worldName));
				sender.sendMessage(ChatColor.YELLOW + "There are" + ChatColor.RED + " " + total + ChatColor.YELLOW + " plots for the world '" + ChatColor.RED + worldName + ChatColor.YELLOW + "'");
			}
			else
			{
				List<World> icWorlds = plugin.getIcUtils().getInfiniteClaimsWorlds();
				sender.sendMessage("Here are all the current InfiniteClaims worlds and their total plots:");
				int index = 1;

				for (World world : icWorlds)
				{
					sender.sendMessage(ChatColor.YELLOW + "" + (index) + ". " + ChatColor.RED + world.getName() + ChatColor.YELLOW + " has a total of " + ChatColor.RED + plugin.getIcUtils().getNumberPlotsForWorld(world) + ChatColor.YELLOW + " plots.");
					index++;
				}
			}
		}
		else if (option.equalsIgnoreCase("plot"))
		{
			ConversationFactory convo = new ConversationFactory(plugin).withModality(true)
			.withFirstPrompt(new WhichPlayerPrompt()).withEscapeSequence("/Cancel").withTimeout(10).thatExcludesNonPlayersWithMessage("You must be logged in to use this command.");
			convo.buildConversation((Conversable) sender).begin();

		}
		else
		{
			sender.sendMessage(plugin.getPluginPrefix() + "Please provide a valid option! " + ChatColor.RED + "[WORLD|PLOTS|PLOT]");

		}
	}

	private class WhichPlayerPrompt extends StringPrompt
	{

		public Prompt acceptInput(ConversationContext arg0, String arg1)
		{
			arg0.setSessionData("owner", arg1);
			return new WhichWorldPrompt();
		}

		public String getPromptText(ConversationContext arg0)
		{
			return plugin.getPluginPrefix() + "Who owns the plot?";
		}

		private class WhichWorldPrompt extends FixedSetPrompt
		{
			public WhichWorldPrompt()
			{
				super(Arrays.copyOf(plugin.getIcUtils().getInfiniteClaimsWorldNames().toArray(), plugin.getIcUtils().getInfiniteClaimsWorldNames().toArray().length, String[].class));
			}

			protected Prompt acceptValidatedInput(ConversationContext arg0, String arg1)
			{
				if (arg1.equalsIgnoreCase("Cancel"))
				{
					return Prompt.END_OF_CONVERSATION;
				}
				arg0.setSessionData("world", arg1);
				return new WhichPlotPrompt(arg0);
			}

			public String getPromptText(ConversationContext arg0)
			{
				return plugin.getPluginPrefix() + "Which world is the plot in? " + ChatColor.RED + formatFixedSet();
			}

			private class WhichPlotPrompt extends FixedSetPrompt
			{
				public WhichPlotPrompt(ConversationContext context)
				{
					super(Arrays.copyOf(plugin.getIcUtils().plotNameForPlayer((String) context.getSessionData("owner")).toArray(), plugin.getIcUtils().plotNameForPlayer((String) context.getSessionData("owner")).toArray().length, String[].class));
				}

				public String getPromptText(ConversationContext arg0)
				{
					return plugin.getPluginPrefix() + "Which plot? " + ChatColor.RED + formatFixedSet();
				}

				protected Prompt acceptValidatedInput(ConversationContext arg0, String arg1)
				{
					if (arg1.equalsIgnoreCase("Cancel"))
					{
						return Prompt.END_OF_CONVERSATION;
					}
					arg0.setSessionData("plot", arg1);
					return new PlotInformationPrompt();
				}

				private class PlotInformationPrompt extends MessagePrompt
				{

					public String getPromptText(ConversationContext arg0)
					{
						StringBuilder builder = new StringBuilder();
						builder.append(plugin.getPluginPrefix() + ChatColor.YELLOW + "Plot Information:\n");
						builder.append(ChatColor.YELLOW + "Name: " + ChatColor.RED + (String) arg0.getSessionData("plot"));
						builder.append("\n");
						RegionManager mgr = plugin.getWorldGuard().getRegionManager(plugin.getServer().getWorld((String) arg0.getSessionData("world")));

						String members = "";
						String owner = null;

						try
						{
							ProtectedRegion plot = mgr.getRegion((String) arg0.getSessionData("owner") + (String) arg0.getSessionData("plot"));
							members = plot.getMembers().toPlayersString();
							owner = plot.getOwners().toPlayersString();
						}
						catch (NullPointerException npe)
						{
						}
						builder.append(ChatColor.YELLOW + "Owner: " + ChatColor.RED + owner);
						builder.append("\n");
						builder.append(ChatColor.YELLOW + "Members: " + ChatColor.RED + members);
						return builder.toString();
					}

					protected Prompt getNextPrompt(ConversationContext arg0)
					{
						return Prompt.END_OF_CONVERSATION;
					}

				}
			}
		}
	}
}
