package com.hskrasek.InfiniteClaims.configuration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.YamlConfigurationOptions;

import com.hskrasek.InfiniteClaims.InfiniteClaims;

public class InfiniteClaimsConfig
{
	private YamlConfiguration			config;
	private YamlConfigurationOptions	configOptions;
	private HashMap<String, Object>		configDefaults	= new HashMap<String, Object>();
	private InfiniteClaims				plugin;
	private File						configFile;

	public InfiniteClaimsConfig(File configFile, InfiniteClaims instance)
	{
		plugin = instance;
		this.configFile = configFile;
		config = new YamlConfiguration();
		configOptions = config.options();
		configDefaults.put("debugging", false);
		configDefaults.put("plots.height", 20);
		configDefaults.put("plots.max-plots", 1);
		configDefaults.put("signs.enabled", true);
		configDefaults.put("signs.placement", "entrance");
		configDefaults.put("signs.prefix", "Plot Owner:");
		configDefaults.put("signs.prefix-color", "0");
		configDefaults.put("signs.owner-color", "0");
		configDefaults.put("version", "2.1.0");
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
	}

	public boolean contains(String key)
	{
		return config.contains(key);
	}

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

	public boolean getBoolean(String key)
	{
		if (!configDefaults.containsKey(key))
		{
			return false;
		}

		return config.getBoolean(key, (Boolean) this.configDefaults.get(key));
	}

	public String getString(String key)
	{
		if (!configDefaults.containsKey(key))
		{
			return "";
		}
		return config.getString(key, (String) configDefaults.get(key));
	}

	public int getInt(String key)
	{
		if (!configDefaults.containsKey(key))
		{
			return 0;
		}

		return config.getInt(key, (Integer) configDefaults.get(key));
	}

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
}
