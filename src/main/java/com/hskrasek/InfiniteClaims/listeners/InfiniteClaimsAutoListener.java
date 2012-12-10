package com.hskrasek.InfiniteClaims.listeners;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.event.player.PlayerChangedWorldEvent;

import com.hskrasek.InfiniteClaims.InfiniteClaims;

import uk.co.jacekk.bukkit.infiniteplots.PlotsGenerator;

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

		if (cg instanceof PlotsGenerator && plugin.getPermissions().hasPermission(p, "iclaims.plot.auto", false))
		{
			int plotSize = ((PlotsGenerator) cg).getPlotSize();
			plugin.icUtils.plotAssigner(w, p, plugin.plotHeight, plotSize, true);
		}
	}
}
