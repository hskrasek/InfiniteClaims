package com.hskrasek.InfiniteClaims.listeners;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.hskrasek.InfiniteClaims.InfiniteClaims;

import uk.co.jacekk.bukkit.infiniteplots.InfinitePlotsGenerator;

public class InfiniteClaimsAutoListener implements Listener
{
	InfiniteClaims	plugin;

	public InfiniteClaimsAutoListener(InfiniteClaims instance)
	{
		this.plugin = instance;
	}

	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent changedWorld)
	{
		Player p = changedWorld.getPlayer();
		World w = p.getWorld();
		ChunkGenerator cg = w.getGenerator();

		plugin.log.log(Level.INFO, "Player '" + p.getName() + "' changed to world: " + w.getName() + " from: " + changedWorld.getFrom().getName());
		if (!(cg instanceof InfinitePlotsGenerator) && !plugin.getIcUtils().isInfiniteClaimsWorld(w))
		{
			plugin.log.log(Level.WARNING, "The world '" + w.getName() + "' is NOT an InfinitePlots world!");
		}
		else
		{
			plugin.log.log(Level.FINE, "The world '" + w.getName() + "' is a InfinitePlots world.");
		}

		if (cg instanceof InfinitePlotsGenerator && plugin.getPermissions().hasPermission(p, "iclaims.plot.auto", false))
		{
			int plotSize = ((InfinitePlotsGenerator) cg).getPlotSize();

			plugin.log.log(Level.FINE, "Automatically finding '" + p.getName() + "' a plot in '" + w.getName() + "'.");

			plugin.icUtils.plotAssigner(w, p, plugin.plotHeight, plotSize, true);
		}
		else if (cg instanceof InfinitePlotsGenerator && !plugin.getPermissions().hasPermission(p, "iclaims.plot.auto", false))
		{
			plugin.log.log(Level.FINE, "Player '" + p.getName() + "' does not have 'iclaims.plot.auto'. Not Automatically assigning a plot.");
		}
	}
}
