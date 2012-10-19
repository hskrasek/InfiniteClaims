package com.hskrasek.InfiniteClaims.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.ChunkGenerator;

import uk.co.jacekk.bukkit.infiniteplots.PlotsGenerator;

import com.hskrasek.InfiniteClaims.InfiniteClaims;
import com.hskrasek.InfiniteClaims.configuration.InfiniteClaimsPlotConfig;
import com.sk89q.worldguard.protection.GlobalRegionManager;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.flags.InvalidFlagFormat;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.GlobalProtectedRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class InfiniteClaimsNewWorld implements Listener
{
	private InfiniteClaims	plugin;

	public InfiniteClaimsNewWorld(InfiniteClaims plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void onNewWorldCreation(WorldLoadEvent newWorld)
	{
		ChunkGenerator cg = newWorld.getWorld().getGenerator();
		if (cg instanceof PlotsGenerator)
		{
			this.plugin.log.info("Creating a plots file for new InfinitePlots world " + newWorld.getWorld().getName());
			InfiniteClaimsPlotConfig plotFile = new InfiniteClaimsPlotConfig(this.plugin, newWorld.getWorld());
			plugin.getIcUtils().getInfiniteClaimsWorlds().add(newWorld.getWorld());
			this.plugin.log.info("Plot file created!");

			ProtectedRegion global = new GlobalProtectedRegion("__global__");
			final StateFlag BUILD = new StateFlag("build", true, RegionGroup.NONE);
			try
			{
				global.setFlag(BUILD, BUILD.parseInput(plugin.getWorldGuard(), null, "deny"));
			}
			catch (InvalidFlagFormat e)
			{
				e.printStackTrace();
			}

			GlobalRegionManager gmgr = plugin.getWorldGuard().getGlobalRegionManager();
			RegionManager rmgr = gmgr.get(newWorld.getWorld());
			rmgr.addRegion(global);

			try
			{
				rmgr.save();
			}
			catch (ProtectionDatabaseException e)
			{
				plugin.log.warning("Could not add the '__global__' region for you. You will need to add it yourself.");
				e.printStackTrace();
			}
		}
	}
}
