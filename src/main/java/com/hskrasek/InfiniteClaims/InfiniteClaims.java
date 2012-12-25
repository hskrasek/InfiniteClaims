package com.hskrasek.InfiniteClaims;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;
import org.mcstats.Metrics.Graph;
import org.mcstats.Metrics.Plotter;

import com.dumptruckman.minecraft.pluginbase.logging.LoggablePlugin;
import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.hskrasek.InfiniteClaims.commands.HelpCommand;
import com.hskrasek.InfiniteClaims.commands.NewPlotCommand;
import com.hskrasek.InfiniteClaims.commands.PlotAdminAddMemberCommand;
import com.hskrasek.InfiniteClaims.commands.PlotAdminCommand;
import com.hskrasek.InfiniteClaims.commands.PlotAdminConfigurationCommand;
import com.hskrasek.InfiniteClaims.commands.PlotAdminInfoCommand;
import com.hskrasek.InfiniteClaims.commands.PlotAdminRemoveCommand;
import com.hskrasek.InfiniteClaims.commands.PlotAdminRemoveMemberCommand;
import com.hskrasek.InfiniteClaims.commands.PlotListCommand;
import com.hskrasek.InfiniteClaims.commands.PlotManagementAddMemberCommand;
import com.hskrasek.InfiniteClaims.commands.PlotManagementCommand;
import com.hskrasek.InfiniteClaims.commands.PlotManagementInfoCommand;
import com.hskrasek.InfiniteClaims.commands.PlotManagementRemoveCommand;
import com.hskrasek.InfiniteClaims.commands.PlotManagementRemoveMemberCommand;
import com.hskrasek.InfiniteClaims.commands.PlotManagementResetCommand;
import com.hskrasek.InfiniteClaims.commands.PlotTeleportCommand;
import com.hskrasek.InfiniteClaims.commands.ReloadCommand;
import com.hskrasek.InfiniteClaims.configuration.InfiniteClaimsConfig;
import com.hskrasek.InfiniteClaims.configuration.InfiniteClaimsPlotConfig;
import com.hskrasek.InfiniteClaims.listeners.InfiniteClaimsAutoListener;
import com.hskrasek.InfiniteClaims.listeners.InfiniteClaimsNewWorld;
import com.hskrasek.InfiniteClaims.utils.IClaimsMessages;
import com.hskrasek.InfiniteClaims.utils.InfiniteClaimsUtilities;
import com.pneumaticraft.commandhandler.CommandHandler;
import com.sk89q.wepif.PermissionsResolverManager;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class InfiniteClaims extends JavaPlugin implements LoggablePlugin
{
	public static final Logger			LOGGER			= Logger.getLogger("Minecraft");
	public Logging				log;
	private CommandHandler				commandHandler;
	private InfiniteClaimsPerms			permissionsInterface;
	public PluginManager				pluginManager;
	public InfiniteClaimsConfig		config;
	public InfiniteClaimsUtilities		icUtils;
	public int							roadOffsetX		= 4;
	public int							roadOffsetZ		= 4;
	public int							plotHeight		= 0;
	public int							maxPlots		= 1;
	public String						ownerSignPrefix;
	public String						signPlacementMethod;
	public ChatColor					prefixColor;
	public ChatColor					ownerColor;
	public boolean						signsEnabled;
	public String						pluginPrefix	= ChatColor.WHITE + "[" + ChatColor.RED + "InfiniteClaims" + ChatColor.WHITE + "] ";
	public PermissionsResolverManager	permissionManager;
	public IClaimsMessages				messages;

	public void onLoad()
	{
		pluginManager = this.getServer().getPluginManager();
		config = new InfiniteClaimsConfig(new File(this.getDataFolder().getAbsolutePath() + File.separator + "config.yml"), this);
		Logging.init(this);
		Logging.setDebugLevel((Integer)config.get("debugging"));
		messages = new IClaimsMessages(this);
		icUtils = new InfiniteClaimsUtilities(this);
		permissionsInterface = new InfiniteClaimsPerms(this);
		commandHandler = new CommandHandler(this, permissionsInterface);
		icUtils.getInfiniteClaimsWorlds();
		this.registerCommands();
		this.setupPlotWorlds();
		this.setupMetrics();
	}

	public void onDisable()
	{
		Logging.log(Level.INFO, "Disabled!");
		Logging.shutdown();
		log = null;
		config = null;
		icUtils = null;
		permissionsInterface = null;
		commandHandler = null;
	}

	public void onEnable()
	{
		pluginManager.registerEvents(new InfiniteClaimsAutoListener(this), this);
		pluginManager.registerEvents(new InfiniteClaimsNewWorld(this), this);
		signsEnabled = (Boolean)config.get("signs.enabled");
		ownerSignPrefix = (String)config.get("signs.prefix");
		signPlacementMethod = (String)config.get("signs.placement");
		prefixColor = ChatColor.getByChar((String)config.get("signs.prefix-color"));
		ownerColor = ChatColor.getByChar((String)config.get("signs.owner-color"));
		plotHeight = (Integer)config.get("plots.height");

//		this.getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Updater(this), 40, 432000);
		getServer().getScheduler().runTaskAsynchronously(this, new Updater(this));

		Logging.log(Level.INFO, "Enabled!");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
	{
		ArrayList<String> allArgs = new ArrayList<String>(Arrays.asList(args));
		allArgs.add(0, cmd.getName());
		return this.commandHandler.locateAndRunCommand(sender, allArgs);
	}

	private void registerCommands()
	{
		this.commandHandler.registerCommand(new PlotTeleportCommand(this));
		this.commandHandler.registerCommand(new HelpCommand(this));
		this.commandHandler.registerCommand(new PlotManagementCommand(this));
		this.commandHandler.registerCommand(new PlotManagementAddMemberCommand(this));
		this.commandHandler.registerCommand(new PlotManagementRemoveMemberCommand(this));
		this.commandHandler.registerCommand(new PlotManagementInfoCommand(this));
		this.commandHandler.registerCommand(new PlotManagementRemoveCommand(this));
		this.commandHandler.registerCommand(new NewPlotCommand(this));
		this.commandHandler.registerCommand(new PlotListCommand(this));
		this.commandHandler.registerCommand(new ReloadCommand(this));
		this.commandHandler.registerCommand(new PlotManagementResetCommand(this));

		this.commandHandler.registerCommand(new PlotAdminCommand(this));
		this.commandHandler.registerCommand(new PlotAdminRemoveCommand(this));
		this.commandHandler.registerCommand(new PlotAdminInfoCommand(this));
		this.commandHandler.registerCommand(new PlotAdminAddMemberCommand(this));
		this.commandHandler.registerCommand(new PlotAdminRemoveMemberCommand(this));
		this.commandHandler.registerCommand(new PlotAdminConfigurationCommand(this));
	}

	private void setupPlotWorlds()
	{
		List<World> plotWorlds = icUtils.getInfiniteClaimsWorlds();

		for (World plotWorld : plotWorlds)
		{
			@SuppressWarnings("unused")
			InfiniteClaimsPlotConfig plotFile = new InfiniteClaimsPlotConfig(this, plotWorld);
		}
	}

	private void setupMetrics()
	{
		try
		{
			Metrics icMetrics = new Metrics(this);

			icMetrics.addCustomData(new Metrics.Plotter("Number of Plots") {

				public int getValue()
				{
					return icUtils.getTotalNumberPlots();
				}
			});

			icMetrics.addCustomData(new Metrics.Plotter("Total Plot Worlds") {

				public int getValue()
				{
					return icUtils.getInfiniteClaimsWorlds().size();
				}
			});

			Graph worldNameGraph = icMetrics.createGraph("Plot World Names");

			for (World plotWorld : icUtils.getInfiniteClaimsWorlds())
			{
				worldNameGraph.addPlotter(new Metrics.Plotter(plotWorld.getName()) {
					public int getValue()
					{
						return 1;
					}
				});
			}

			Iterator<Plotter> plotterIt = worldNameGraph.getPlotters().iterator();
			while (plotterIt.hasNext())
			{
				Plotter currentPlotter = plotterIt.next();
				Logging.log(Level.INFO, currentPlotter.getColumnName());
			}
			icMetrics.addGraph(worldNameGraph);
			icMetrics.start();
			Logging.info("Metrics started! To opt-out go to /plugins/PluginMetrics/config.yml and change opt-out to true.");
		}
		catch (IOException e)
		{
			Logging.warning(e.getMessage());
			e.printStackTrace();
		}
	}

	public String getPluginPrefix()
	{
		return pluginPrefix;
	}

	public CommandHandler getCommandHandler()
	{
		return commandHandler;
	}

	public Logging getLog()
	{
		return log;
	}

	public InfiniteClaimsUtilities getIcUtils()
	{
		return icUtils;
	}

	public WorldGuardPlugin getWorldGuard()
	{
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldGuard");

		/**
		 * @TODO: Possibly remove this code, for it may no longer be necessary
		 */
		if (plugin == null || !(plugin instanceof WorldGuardPlugin))
		{
			Logging.severe("WorldEdit MUST BE INSTALLED!");
			return null;
		}
		return (WorldGuardPlugin) plugin;
	}

	public WorldEditPlugin getWorldEdit()
	{
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");

		/**
		 * @TODO: Possibly remove this code, for it may no longer be necessary
		 */
		if (plugin == null || !(plugin instanceof WorldEditPlugin))
		{
			Logging.severe("WorlEdit MUST BE INSTALLED");
			return null;
		}
		return (WorldEditPlugin) plugin;
	}

	public InfiniteClaimsPerms getPermissions()
	{
		return permissionsInterface;
	}

	private class Updater implements Runnable
	{
		InfiniteClaims	plugin;

		public Updater(InfiniteClaims plugin)
		{
			this.plugin = plugin;
		}

		public void run()
		{
			try
			{
				final String address = "http://updates.hunterskrasek.com/updates.php?version=" + this.plugin.getDescription().getVersion();
				final URL url = new URL(address.replace(" ", "%20"));
				final URLConnection urlConnection = url.openConnection();
				urlConnection.setConnectTimeout(8000);
				urlConnection.setReadTimeout(15000);
				urlConnection.setRequestProperty("Version", this.plugin.getDescription().getVersion());
				final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
				String tempVersion;

				if ((tempVersion = bufferedReader.readLine()) != null)
				{
					if (plugin.getDescription().getVersion().contains("SNAPSHOT"))
					{
						return;
					}
					if (!this.plugin.getDescription().getVersion().equals(tempVersion))
					{
						Logging.info("Found a different version available: " + tempVersion);
						Logging.info("You can get it at http://dev.bukkit.org/server-mods/infiniteclaims");
					}
					bufferedReader.close();
					urlConnection.getInputStream().close();
					return;
				}
				else
				{
					bufferedReader.close();
					urlConnection.getInputStream().close();
				}
			}
			catch (final Exception e)
			{
				Logging.warning("Could not check if InfiniteClaims is up-to-date, will try again later.");
			}
		}

	}
}