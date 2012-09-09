package com.hskrasek.InfiniteClaims.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import com.hskrasek.InfiniteClaims.InfiniteClaims;
import com.hskrasek.InfiniteClaims.utils.InfiniteClaimsUtilities;
import com.pneumaticraft.commandhandler.CommandHandler;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class PlotListCommand extends PaginatedCoreCommand<String>
{
	InfiniteClaims			plugin;
	InfiniteClaimsUtilities	icUtils;

	public PlotListCommand(InfiniteClaims plugin)
	{
		super(plugin);
		this.plugin = plugin;
		this.icUtils = plugin.getIcUtils();
		this.setName("Plot Listing");
		this.setCommandUsage(String.format("%s/iclaims list [ALL|HERE|WORLD] [PAGE]", ChatColor.YELLOW, ChatColor.RED));
		this.setArgRange(0, 2);
		this.addKey("icplotlist");
		this.addKey("iclist");
		this.addKey("iclaims list");
		this.setPermission("iclaims.plot.list", "List all available plots in the current world", PermissionDefault.OP);
		this.addCommandExample(String.format("%s/iclaims list", ChatColor.YELLOW, ChatColor.RED));
		this.addCommandExample(String.format("%s/iclaims list %shere", ChatColor.YELLOW, ChatColor.RED));
		this.addCommandExample(String.format("%s/iclaims list %sCreative 2", ChatColor.YELLOW, ChatColor.RED));
		this.setItemsPerPage(5);
	}

	private List<String> getFancyPlotList(Player plotOwner, World plotWorld)
	{
		List<String> plotList = new ArrayList<String>();

		final RegionManager mgr = plugin.getWorldGuard().getGlobalRegionManager().get(plotWorld);
		final Map<String, ProtectedRegion> regions = mgr.getRegions();
		LocalPlayer localPlayer = plugin.getWorldGuard().wrapPlayer(plotOwner);

		for (String id : regions.keySet())
		{
			if (regions.get(id).isOwner(localPlayer))
			{
				try
				{
					plotList.add(ChatColor.YELLOW + (id.substring(id.indexOf("plot"))) + ChatColor.RED + " - " + plotWorld.getName());
				}
				catch (StringIndexOutOfBoundsException e)
				{
					continue;
				}
			}
		}

		Collections.sort(plotList);
		return plotList;
	}

	private List<String> getAllFancyPlotList(Player plotOwner)
	{
		List<String> plotList = new ArrayList<String>();

		List<World> plotWorlds = icUtils.getInfiniteClaimsWorlds();

		for (World world : plotWorlds)
		{
			final RegionManager mgr = plugin.getWorldGuard().getGlobalRegionManager().get(world);
			final Map<String, ProtectedRegion> regions = mgr.getRegions();
			LocalPlayer localPlayer = plugin.getWorldGuard().wrapPlayer(plotOwner);

			for (String id : regions.keySet())
			{
				if (regions.get(id).isOwner(localPlayer))
				{
					try
					{
						plotList.add(ChatColor.YELLOW + (id.substring(id.indexOf("plot"))) + ChatColor.RED + " - " + world.getName());
					}
					catch (StringIndexOutOfBoundsException e)
					{
						continue;
					}
				}
			}
		}

		// Collections.sort(plotList);
		return plotList;
	}

	protected List<String> getFilteredItems(List<String> availableItems, String filter)
	{
		List<String> filtered = new ArrayList<String>();

		for (String s : availableItems)
		{
			if (s.matches("(?i).*" + filter + ".*"))
			{
				filtered.add(s);
			}
		}

		return filtered;
	}

	protected String getItemText(String item)
	{
		return item;
	}

	public void runCommand(CommandSender sender, List<String> args)
	{
		sender.sendMessage(ChatColor.YELLOW + "=== InfiniteClaims Plot List ===");

		Player player = null;

		if (sender instanceof Player)
		{
			player = (Player) sender;
		}

		FilterObject filterObject = this.getPageAndFilter(args);

		List<String> availablePlots = new ArrayList<String>(this.getAllFancyPlotList(player));

		if (filterObject.getFilter().length() > 0)
		{
			if (filterObject.getFilter().equalsIgnoreCase("here"))
			{
				filterObject.setString(player.getWorld().getName());
			}
			else if (!filterObject.getFilter().equalsIgnoreCase("all"))
			{
				availablePlots = this.getFilteredItems(availablePlots, filterObject.getFilter());
			}
			else
			{
				availablePlots = this.getAllFancyPlotList(player);
			}

			if (availablePlots.size() == 0)
			{
				sender.sendMessage("Sorry... No plots matched your filter: " + filterObject.getFilter());
			}
		}

		int totalPages = (int) Math.ceil(availablePlots.size() / (this.itemsPerPage + 0.0));

		if (filterObject.getPage() > totalPages)
		{
			filterObject.setPage(totalPages);
		}

		sender.sendMessage(String.format("%s Page %s%d %sof %s%d", ChatColor.YELLOW, ChatColor.RED, filterObject.getPage(), ChatColor.YELLOW, ChatColor.RED, totalPages));
		this.showPage(filterObject.getPage(), sender, availablePlots);

		// if(availablePlots.size() == 0)
		// {
		// player.sendMessage(plugin.getPluginPrefix() + ChatColor.YELLOW +
		// "You have no plots in this world");
		// return;
		// }

		// for(String plot : availablePlots)
		// {
		// player.sendMessage(plot);
		// }
	}

}
