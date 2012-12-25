package com.hskrasek.InfiniteClaims.configuration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;

import com.dumptruckman.minecraft.pluginbase.logging.Logging;
import com.hskrasek.InfiniteClaims.InfiniteClaims;
import com.hskrasek.InfiniteClaims.exceptions.PropertyNotFoundException;

public class InfiniteClaimsConfig
{
	private YamlConfiguration			config;
	private YamlConfigurationOptions	configOptions;
	private HashMap<String, Object>		configDefaults	= new HashMap<String, Object>();
	private InfiniteClaims				plugin;
	private File						configFile;
	private Map<String, Integer> multiplePlotMaxes;

	public InfiniteClaimsConfig(File configFile, InfiniteClaims instance)
	{
		plugin = instance;
		Logging.init(plugin);
		this.configFile = configFile;
		config = new YamlConfiguration();
		configOptions = config.options();
		configDefaults.put("debugging", 0);
		configDefaults.put("plots.height", 20);
		configDefaults.put("plots.multiple.default", 1);
		configDefaults.put("plots.max-plots", 1);
		configDefaults.put("plots.multiple.default", "1");
		configDefaults.put("plots.multiple.vip", "1");
		configDefaults.put("plots.multiple.donor", "1");
		configDefaults.put("signs.enabled", true);
		configDefaults.put("signs.placement", "entrance");
		configDefaults.put("signs.prefix", "Plot Owner:");
		configDefaults.put("signs.prefix-color", "0");
		configDefaults.put("signs.owner-color", "0");
		configDefaults.put("version", "2.1.2-SNAPSHOT");
		String header = this.getHeader();
		configOptions.header(header);
		configOptions.copyHeader(true);
		if (configFile.exists() == false)
		{
			for (String key : configDefaults.keySet())
			{
				config.set(key, configDefaults.get(key));
			}

			try
			{
				config.save(configFile);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				config.load(configFile);
				if (config.getString("version") != configDefaults.get("version"))
				{
					config.set("version", configDefaults.get("version"));
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		
		registerPlotMaxPermissions();
	}

	public boolean contains(String key)
	{
		return config.contains(key);
	}

	@Deprecated
	public void setString(String key, String value)
	{
		config.set(key, value);
		try
		{
			config.save(configFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void set(String key, Object value) throws PropertyNotFoundException
	{
		if(!config.contains(key))
		{
			throw new PropertyNotFoundException();
		}
		config.set(key, value);
		try
		{
			config.save(configFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Object get(String key)
	{
		if(!configDefaults.containsKey(key))
		{
			return null;
		}
		
		return config.get(key);
	}
	
	public Set<String> getKeys()
	{
		return config.getKeys(true);
	}

	@Deprecated
	public boolean getBoolean(String key)
	{
		if (!configDefaults.containsKey(key))
		{
			return false;
		}

		return config.getBoolean(key, (Boolean) this.configDefaults.get(key));
	}

	@Deprecated
	public String getString(String key)
	{
		if (!configDefaults.containsKey(key))
		{
			return "";
		}
		return config.getString(key, (String) configDefaults.get(key));
	}

	@Deprecated
	public int getInt(String key)
	{
		if (!configDefaults.containsKey(key))
		{
			return 0;
		}

		return config.getInt(key, (Integer) configDefaults.get(key));
	}

	@Deprecated
	public double getDouble(String key)
	{
		if (!configDefaults.containsKey(key))
		{
			return 0.0;
		}

		return config.getDouble(key, (Double) configDefaults.get(key));
	}

	private String getHeader()
	{
		String header = "####################################################################\n" + "                InfiniteClaims Configuration File                  #\n" + " For an explanation on configuring InfiniteClaims, how to set it   #\n" + " or have no clue what that setting value is, please visit          #\n" + "        http://dev.bukkit.org/server-mods/infiniteclaims           #\n" + "####################################################################\n";

		return header;
	}
	
	private int getMaxForPermission(String perm)
	{
		if(perm != null)
		{
			return multiplePlotMaxes.get(perm);
		}
		return 1;
	}
	
	public int getPlayerMaxPlots(Player player)
	{
		Set<String> permissions = multiplePlotMaxes.keySet();
		String highestPerm = null;
		for(String perm : permissions)
		{
			if(player.hasPermission(perm) && highestPerm == null)
			{
				highestPerm = perm;
			}
			if(getMaxForPermission(highestPerm) < getMaxForPermission(perm))
			{
				highestPerm = perm;
			}
		}
		
		if(highestPerm != null)
		{
			return getMaxForPermission(highestPerm);
		}
		return 1;
	}
	
	
	private void registerPlotMaxPermissions()
	{
		ConfigurationSection plotMultiples = config.getConfigurationSection("plots.multiple");
		multiplePlotMaxes = new HashMap<String, Integer>();
		Logging.info("Setting up multiple plot permissions", null);
		for(String key : plotMultiples.getKeys(true))
		{
			multiplePlotMaxes.put(String.format("iclaims.plot.max.%s", key), Integer.parseInt(plotMultiples.getString(key)));
			registerPlotMaxPermission(String.format("iclaims.plot.max.%s", key));
		}
	}
	
	private void registerPlotMaxPermission(String permission)
	{
		plugin.pluginManager.addPermission(new Permission(permission, PermissionDefault.FALSE));
		Logging.info("Registered permission: %s", permission);
	}
}
