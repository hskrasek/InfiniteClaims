package com.hskrasek.InfiniteClaims.configuration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;

import uk.co.jacekk.bukkit.infiniteplots.InfinitePlotsGenerator;

import com.hskrasek.InfiniteClaims.InfiniteClaims;
import com.hskrasek.InfiniteClaims.exceptions.PlayerNotFoundException;
import com.hskrasek.InfiniteClaims.exceptions.PlotNotFoundException;

public class InfiniteClaimsPlotConfig
{
	private YamlConfiguration			plot;
	private File						plotFile;
	private YamlConfigurationOptions	plotOptions;
	private HashMap<String, Object>		plotDefaults	= new HashMap<String, Object>();
	private InfiniteClaims				plugin;
	private World						plotWorld;

	public InfiniteClaimsPlotConfig(InfiniteClaims plugin, World plotWorld)
	{
		plot = new YamlConfiguration();
		this.plugin = plugin;
		this.plotWorld = plotWorld;
		if (plotWorld.getGenerator() instanceof InfinitePlotsGenerator)
		{
			plotFile = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + plotWorld.getName() + File.separator + "plots.yml");
		}

		plotOptions = plot.options();
		plotDefaults.put("plots", "");

		String header = this.getHeader();
		plotOptions.header(header);

		if (!plotFile.exists())
		{
			plotOptions.copyHeader(true);
			for (String key : plotDefaults.keySet())
			{
				plot.set(key, plotDefaults.get(key));
			}

			try
			{
				plot.save(plotFile);
			}
			catch (IOException e)
			{
				this.plugin.log.severe("Could not create a plot file for the World '" + plotWorld.getName() + "'. Disabling InfiniteClaims!");
				this.plugin.getPluginLoader().disablePlugin(plugin);
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				plot.load(plotFile);
			}
			catch (Exception e)
			{
				this.plugin.log.severe("Could not load the plots for the World '" + plotWorld.getName() + "'. Disabling InfiniteClaims!");
				this.plugin.getPluginLoader().disablePlugin(plugin);
				e.printStackTrace();
			}
		}
	}

	public void setPlot(String playerName, String plotName, List<Double> coords)
	{
		plot.set("plots." + playerName.toLowerCase() + "." + plotName + ".x", coords.get(0));
		plot.set("plots." + playerName.toLowerCase() + "." + plotName + ".z", coords.get(1));
		this.save();
	}

	public Location getPlot(String playerName, String plotName) throws PlotNotFoundException, PlayerNotFoundException
	{
		if (plot.get("plots." + playerName) == null)
		{
			throw new PlayerNotFoundException("Could not locate a plot for '" + playerName + "'");
		}

		double x = plot.getDouble("plots." + playerName.toLowerCase() + "." + plotName + ".x");
		double y = plugin.plotHeight + 2;
		double z = plot.getDouble("plots." + playerName.toLowerCase() + "." + plotName + ".z");

		if (x == 0 && z == 0)
		{
			throw new PlotNotFoundException("Could not find a plot by the name '" + plotName + "' for '" + playerName + "'");
		}
		else
		{
			return new Location(this.plotWorld, x, y, z, 180, 0);
		}
	}

	public void removePlot(String player)
	{
		plot.set("plots." + player, null);
		this.save();
	}

	private String getHeader()
	{
		String header = "##################################################################\n" + "                     InfiniteClaims Plot File                    #\n" + "This file contains all players plot within this world. Please do #\n" + "not modify it unless absolutely need to. If you do not verify    #\n" + "that the format is correct, you face breaking plot teleportation.#\n" + "You can verify YAML format at: http://tinyurl.com/yamlic         #\n" + "##################################################################\n";

		return header;
	}

	private void save()
	{
		try
		{
			plot.save(plotFile);
		}
		catch (IOException e)
		{
			this.plugin.log.severe("Could not save the plot file for '" + plotWorld.getName() + "'.");
			e.printStackTrace();
		}
	}
}
