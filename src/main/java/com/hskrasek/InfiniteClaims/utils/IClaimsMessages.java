package com.hskrasek.InfiniteClaims.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

import com.hskrasek.InfiniteClaims.InfiniteClaims;

public class IClaimsMessages
{
	InfiniteClaims plugin;
	YamlConfiguration messages = null;
	
	public IClaimsMessages(InfiniteClaims plugin)
	{
		this.plugin = plugin;
		
		if(!new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "messages.yml").exists())
		{
			copyMessagesToPluginDir();
		}
		else if(!currentMessageVersion().equals(userMessageVersion()))
		{
			
		}
		else
		{
			messages = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "messages.yml"));
		}
		
//		YamlConfiguration currentMessages = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "messages.yml"));
//		for(String key : currentMessages.getKeys(true))
//		{
//			plugin.log.log(Level.INFO, key);
//		}
	}
	
	private void copyMessagesToPluginDir()
	{
		try
		{
			File jarLocation = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getCanonicalFile();
			
			if(jarLocation.isFile())
			{
				JarFile jarFile = new JarFile(jarLocation);
				JarEntry jarEntry = jarFile.getJarEntry("messages.yml");
				if(jarEntry != null && !jarEntry.isDirectory())
				{
					InputStream in = jarFile.getInputStream(jarEntry);
					InputStreamReader inReader = new InputStreamReader(in, "UTF8");
					FileOutputStream out = new FileOutputStream(plugin.getDataFolder().getAbsolutePath() + File.separator + "messages.yml");
					OutputStreamWriter outWriter = new OutputStreamWriter(out, "UTF-8");
					char[] tempBytes = new char[512];
					int readBytes = inReader.read(tempBytes, 0, 512);
					while(readBytes > -1)
					{
						outWriter.write(tempBytes, 0, readBytes);
						readBytes = inReader.read(tempBytes, 0, 512);
					}
					outWriter.close();
					inReader.close();
				}
			}
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			plugin.log.log(Level.SEVERE, "Was unable to save the messages.yml to plugin directory. Report following stack trace to the developer.");
			e.printStackTrace();
		}
	}
	
	private String currentMessageVersion()
	{
		return getJarMessageYAML().getString("version");
	}
	
	private String userMessageVersion()
	{
		File userMessageFile = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "messages.yml");
		
		if(userMessageFile != null && !userMessageFile.isDirectory() && userMessageFile.exists())
		{
			YamlConfiguration userMessages = YamlConfiguration.loadConfiguration(userMessageFile);
			return (String)userMessages.get("vesion", "2.1.0");
		}
		
		return null;
	}
	
	protected YamlConfiguration getJarMessageYAML()
	{
		File jarLocation = null;
		try
		{
			jarLocation = new File(plugin.getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getCanonicalFile();
		}
		catch (IOException e)
		{
			plugin.log.log(Level.SEVERE, "Was not able to access the plugin jar while loading the messages.yml");
			e.printStackTrace();
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
		
		if(jarLocation.isFile())
		{
			JarFile jarFile = null;
			try
			{
				jarFile = new JarFile(jarLocation);
			}
			catch (IOException e)
			{
				plugin.log.log(Level.SEVERE, "Was not able to load the InfiniteClaims JAR file.");
				e.printStackTrace();
			}
			JarEntry jarEntry = jarFile.getJarEntry("messages.yml");
			if(jarEntry != null && !jarEntry.isDirectory())
			{
				try
				{
					YamlConfiguration currentMessages = YamlConfiguration.loadConfiguration(jarFile.getInputStream(jarEntry));
					return currentMessages;
				}
				catch (IOException e)
				{
					plugin.log.log(Level.SEVERE, "Was not able to load the messages file from the plugin JAR.");
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
	protected YamlConfiguration getUserMessageYAML()
	{
		return messages;
	}
	
	private void updateUsersMessageFile()
	{
		Set<String> jarKeys = getJarMessageYAML().getKeys(true);
		Set<String> userKeys = getUserMessageYAML().getKeys(true);
		
	}
	
	public String getMessage(String messageKey, Object... args)
	{
		return getFormattedMessage(messageKey, args);
	}
	
	protected String getFormattedMessage(String messageKey, Object... args)
	{
		String message = getUserMessageYAML().getString("messages." + messageKey);
		if(args != null)
		{
			return ChatColor.translateAlternateColorCodes('&', String.format(message, args));
		}
		return ChatColor.translateAlternateColorCodes('&', message);
	}
}
