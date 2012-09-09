/******************************************************************************
 * Multiverse 2 Copyright (c) the Multiverse Team 2011.                       *
 * Multiverse 2 is licensed under the BSD License.                            *
 * For more information please check the README.md file included              *
 * with this project.                                                         *
 ******************************************************************************/

package com.hskrasek.InfiniteClaims.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hskrasek.InfiniteClaims.InfiniteClaims;
import com.pneumaticraft.commandhandler.Command;

public abstract class PaginatedCommand<T> extends Command
{
	private static final int	MAX_ITEMS		= 7;
	protected int				itemsPerPage	= MAX_ITEMS;

	public PaginatedCommand(InfiniteClaims plugin)
	{
		super(plugin);
	}

	protected void setItemsPerPage(int items)
	{
		itemsPerPage = items;
	}

	protected abstract List<T> getFilteredItems(List<T> availableItems, String filter);

	protected String stitchThisString(List<String> list)
	{
		StringBuilder string = new StringBuilder();

		for (String s : list)
		{
			string.append(s);
			string.append(' ');
		}

		return string.toString();
	}

	protected void showPage(int page, CommandSender sender, List<T> cmds)
	{
		page = (page <= 0) ? 1 : page;
		int start = (page - 1) * itemsPerPage;
		int end = start + itemsPerPage;

		for (int i = start; i < end; i++)
		{
			if (i < cmds.size())
			{
				sender.sendMessage(this.getItemText(cmds.get(i)));
			}
			else if (sender instanceof Player)
			{
				sender.sendMessage(" ");
			}
		}
	}

	protected abstract String getItemText(T item);

	protected FilterObject getPageAndFilter(List<String> args)
	{
		int page = 1;

		String filter = "";

		if (args.size() == 0)
		{
			filter = "";
			page = 1;
		}
		else if (args.size() == 1)
		{
			try
			{
				page = Integer.parseInt(args.get(0));
			}
			catch (NumberFormatException e)
			{
				filter = args.get(0);
				page = 1;
			}
		}
		else if (args.size() == 2)
		{
			filter = args.get(0);
			try
			{
				page = Integer.parseInt(args.get(1));
			}
			catch (NumberFormatException e)
			{
				page = 1;
			}
		}

		return new FilterObject(page, filter);
	}

	protected class FilterObject
	{
		private Integer	page;
		private String	filter;

		public FilterObject(Integer page, String filter)
		{
			this.page = page;
			this.filter = filter;
		}

		public Integer getPage()
		{
			return page;
		}

		public void setPage(int page)
		{
			this.page = page;
		}

		public String getFilter()
		{
			return this.filter;
		}

		public void setString(String filter)
		{
			this.filter = filter;
		}
	}
}
