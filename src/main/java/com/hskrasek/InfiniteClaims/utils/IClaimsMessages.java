package com.hskrasek.InfiniteClaims.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

import com.hskrasek.InfiniteClaims.InfiniteClaims;

public class IClaimsMessages
{
	InfiniteClaims plugin;
	
	public IClaimsMessages(InfiniteClaims plugin)
	{
		this.plugin = plugin;
		
		copyMessagesToPluginDir();
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
}
