package com.hskrasek.InfiniteClaims.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

import uk.co.jacekk.bukkit.infiniteplots.InfinitePlotsGenerator;

import com.hskrasek.InfiniteClaims.InfiniteClaims;
import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.databases.ProtectionDatabaseException;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class LegacyConversion implements Runnable
{
	private InfiniteClaims			plugin;
	private InfiniteClaimsUtilities	icUtils;

	public LegacyConversion(InfiniteClaims plugin)
	{
		this.plugin = plugin;
		this.icUtils = plugin.getIcUtils();
	}

	public void run()
	{
		plugin.log.info("Starting legacy conversion...");
		List<World> plotWorlds = icUtils.getInfiniteClaimsWorlds();
		WorldGuardPlugin wgp = plugin.getWorldGuard();

		long overallStartTime = new Date().getTime();

		for (World plotWorld : plotWorlds)
		{
			plugin.log.log(Level.INFO, "Found a InfinitePlots world! Now converting plots for world: " + plotWorld.getName());

			int plotSize = ((InfinitePlotsGenerator) plotWorld.getGenerator()).getPlotSize();

			int y = plugin.plotHeight;

			RegionManager mgr = wgp.getRegionManager(plotWorld);

			Map<String, ProtectedRegion> regions = mgr.getRegions();
			Map<String, ProtectedRegion> modifiedRegions = new HashMap<String, ProtectedRegion>();

			for (String currentPlotId : regions.keySet())
			{
				if (currentPlotId.equalsIgnoreCase("__global__"))
				{
					ProtectedRegion global = mgr.getRegion("__global__");
					modifiedRegions.put("__global__", global);
					continue;
				}

				ProtectedRegion currentPlot = mgr.getRegion(currentPlotId);
				String plotOwner = currentPlot.getOwners().toPlayersString();

				if (plotOwner.contains(","))
				{
					String[] split = plotOwner.split(",");
					plotOwner = split[0];
				}

				BlockVector plotMinimum = currentPlot.getMinimumPoint();
				BlockVector plotMaximum = currentPlot.getMaximumPoint();

				ProtectedRegion convertedPlot = new ProtectedCuboidRegion(plotOwner + "plot" + 1, plotMinimum, plotMaximum);
				convertedPlot.setOwners(currentPlot.getOwners());
				convertedPlot.setMembers(currentPlot.getMembers());
				modifiedRegions.put(plotOwner + "plot" + 1, convertedPlot);

				Location bottomRight = new Location(plotWorld, plotMinimum.getX(), y, plotMinimum.getZ());
				Location topRight = new Location(plotWorld, plotMaximum.getX(), y, plotMaximum.getZ());
				Location bottomLeft = new Location(plotWorld, bottomRight.getX() + (plotSize - 1), y, bottomRight.getZ());
				Location topLeft = new Location(plotWorld, bottomRight.getX() + (plotSize - 1), y, bottomRight.getZ() + (plotSize - 1));

				IClaimsPlayer player = new IClaimsPlayer(plotOwner, plugin);
				int regionCountTemp = 1;
				final int regionCount = mgr.getRegionCountOfPlayer(wgp.wrapPlayer(player));

				icUtils.savePlot(player, "plot" + (regionCountTemp < regionCount ? regionCountTemp++ : regionCount), new Location(plotWorld, bottomRight.getX() + (plotSize / 2), y + 1, topRight.getZ()));

				if (plugin.signPlacementMethod.equals("entrance"))
				{
					Location entranceLocation1 = new Location(plotWorld, bottomRight.getX() + (plotSize / 2) - 2, y + 3, bottomRight.getZ() + (plotSize));
					Location entranceLocation2 = new Location(plotWorld, bottomRight.getX() + (plotSize / 2) + 2, y + 3, bottomRight.getZ() + (plotSize));
					icUtils.placeSign(entranceLocation1, plugin.ownerSignPrefix, player.getName(), BlockFace.SOUTH);
					icUtils.placeSign(entranceLocation2, plugin.ownerSignPrefix, player.getName(), BlockFace.SOUTH);
				}
				else if (plugin.signPlacementMethod.equals("corners"))
				{
					// creates a sign for the bottom right corner
					Location bottomRightTest = new Location(plotWorld, bottomRight.getX() - 1, bottomRight.getY() + 3, bottomRight.getZ() - 1);
					icUtils.placeSign(bottomRightTest, plugin.ownerSignPrefix, player.getName(), BlockFace.NORTH_WEST);

					// creates a sign for the bottom left corner
					Location bottomLeftTest = new Location(plotWorld, bottomLeft.getX() + 1, bottomLeft.getY() + 3, bottomLeft.getZ() - 1);
					icUtils.placeSign(bottomLeftTest, plugin.ownerSignPrefix, player.getName(), BlockFace.NORTH_EAST);

					// creates a sign for the top right corner
					Location topRightSign = new Location(plotWorld, topRight.getX() - 1, topRight.getY() + 3, topRight.getZ() + 1);
					icUtils.placeSign(topRightSign, plugin.ownerSignPrefix, player.getName(), BlockFace.SOUTH_WEST);

					// creates a sign for the top left corner
					Location topLeftSign = new Location(plotWorld, topLeft.getX() + 1, topLeft.getY() + 3, topLeft.getZ() + 1);
					icUtils.placeSign(topLeftSign, plugin.ownerSignPrefix, player.getName(), BlockFace.SOUTH_EAST);
				}
				else if (plugin.signPlacementMethod.equals("both"))
				{
					Location entranceLocation1 = new Location(plotWorld, bottomRight.getX() + (plotSize / 2) - 2, y + 3, bottomRight.getZ() + (plotSize));
					Location entranceLocation2 = new Location(plotWorld, bottomRight.getX() + (plotSize / 2) + 2, y + 3, bottomRight.getZ() + (plotSize));
					icUtils.placeSign(entranceLocation1, plugin.ownerSignPrefix, player.getName(), BlockFace.SOUTH);
					icUtils.placeSign(entranceLocation2, plugin.ownerSignPrefix, player.getName(), BlockFace.SOUTH);

					// creates a sign for the bottom right corner
					Location bottomRightTest = new Location(plotWorld, bottomRight.getX() - 1, bottomRight.getY() + 3, bottomRight.getZ() - 1);
					icUtils.placeSign(bottomRightTest, plugin.ownerSignPrefix, player.getName(), BlockFace.NORTH_WEST);

					// creates a sign for the bottom left corner
					Location bottomLeftTest = new Location(plotWorld, bottomLeft.getX() + 1, bottomLeft.getY() + 3, bottomLeft.getZ() - 1);
					icUtils.placeSign(bottomLeftTest, plugin.ownerSignPrefix, player.getName(), BlockFace.NORTH_EAST);

					// creates a sign for the top right corner
					Location topRightSign = new Location(plotWorld, topRight.getX() - 1, topRight.getY() + 3, topRight.getZ() + 1);
					icUtils.placeSign(topRightSign, plugin.ownerSignPrefix, player.getName(), BlockFace.SOUTH_WEST);

					// creates a sign for the top left corner
					Location topLeftSign = new Location(plotWorld, topLeft.getX() + 1, topLeft.getY() + 3, topLeft.getZ() + 1);
					icUtils.placeSign(topLeftSign, plugin.ownerSignPrefix, player.getName(), BlockFace.SOUTH_EAST);
				}
			}

			mgr.setRegions(modifiedRegions);

			try
			{
				mgr.save();
			}
			catch (ProtectionDatabaseException e)
			{
				e.printStackTrace();
			}
		}

		long overallEndTime = new Date().getTime();

		long overallDifference = overallEndTime - overallStartTime;

		plugin.log.info("Overall it took: " + ((overallDifference / 1000) / 60) + " minutes to convert all InfinitePlot worlds!");
	}
}
