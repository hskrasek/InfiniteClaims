package com.hskrasek.InfiniteClaims.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.conversations.FixedSetPrompt;
import org.bukkit.conversations.MessagePrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.permissions.PermissionDefault;

import com.hskrasek.InfiniteClaims.InfiniteClaims;
import com.hskrasek.InfiniteClaims.utils.InfiniteClaimsUtilities;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.managers.RegionManager;

public class PlotAdminAddMemberCommand extends IClaimsCommand
{
	InfiniteClaims plugin;
	InfiniteClaimsUtilities icUtils;
	
	public PlotAdminAddMemberCommand(InfiniteClaims plugin)
	{
		super(plugin);
		this.plugin = plugin;
		this.icUtils = plugin.getIcUtils();
		this.setName("Plot Admin (Add member)");
		this.setCommandUsage("/icadmin addmember");
		this.setArgRange(0, 1);
		this.addKey("iclaimsadmin addmember");
		this.addKey("icadmin addmember");
		this.addKey("iclaims admin addmember");
		this.addKey("icaam");
		this.addCommandExample("/icladmin addmember");
		this.setPermission("iclaims.plot.admin.addmember", "Add a member to someone's plot.", PermissionDefault.OP);
	}

	public void runCommand(CommandSender sender, List<String> args)
	{
		ConversationFactory convo = new ConversationFactory(plugin).withModality(true)
				.withFirstPrompt(new WhichPlayerPrompt()).withEscapeSequence("/Cancel").withTimeout(10).thatExcludesNonPlayersWithMessage("You must be logged in to use this command.");
				convo.buildConversation((Conversable) sender).begin();
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
			return plugin.getPluginPrefix() + "Plot Owner?";
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
				return plugin.getPluginPrefix() + "Plot World? " + ChatColor.RED + formatFixedSet();
			}

			private class WhichPlotPrompt extends FixedSetPrompt
			{
				public WhichPlotPrompt(ConversationContext context)
				{
					super(Arrays.copyOf(plugin.getIcUtils().plotNameForPlayer((String) context.getSessionData("owner")).toArray(), plugin.getIcUtils().plotNameForPlayer((String) context.getSessionData("owner")).toArray().length, String[].class));
				}

				public String getPromptText(ConversationContext arg0)
				{
					return plugin.getPluginPrefix() + "Which plot?" + ChatColor.RED + formatFixedSet();
				}

				protected Prompt acceptValidatedInput(ConversationContext arg0, String arg1)
				{
					if (arg1.equalsIgnoreCase("Cancel"))
					{
						return Prompt.END_OF_CONVERSATION;
					}
					arg0.setSessionData("plot", arg1);
					return new PlayerToAddPrompt();
				}
				
				private class PlayerToAddPrompt extends StringPrompt
				{

					public Prompt acceptInput(ConversationContext arg0, String arg1)
					{
						arg0.setSessionData("player", arg1);
						return new MemberAddPrompt();
					}

					public String getPromptText(ConversationContext arg0)
					{
						return plugin.getPluginPrefix() + "Player to add?";
					}
					
					private class MemberAddPrompt extends MessagePrompt
					{

						public String getPromptText(ConversationContext arg0)
						{
							RegionManager mgr = plugin.getWorldGuard().getRegionManager(plugin.getServer().getWorld((String)arg0.getSessionData("world")));
							String plotName = (String)arg0.getSessionData("owner") + arg0.getSessionData("plot");
							String playerToAdd = (String)arg0.getSessionData("player");
							DefaultDomain plotMembers = mgr.getRegion(plotName).getMembers();
							plotMembers.addPlayer(playerToAdd);
							mgr.getRegion(plotName).setMembers(plotMembers);
							
							try
							{
								mgr.save();
							}
							catch (ProtectionDatabaseException e)
							{
								e.printStackTrace();
							}
							return "Added '" + ChatColor.YELLOW + playerToAdd + ChatColor.WHITE + "' to plot '" + ChatColor.RED + plotName + ChatColor.WHITE + "'";
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
}
