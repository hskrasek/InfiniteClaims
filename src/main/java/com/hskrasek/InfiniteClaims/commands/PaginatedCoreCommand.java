package com.hskrasek.InfiniteClaims.commands;

import com.hskrasek.InfiniteClaims.InfiniteClaims;

public abstract class PaginatedCoreCommand<T> extends PaginatedCommand<T>
{
	protected InfiniteClaims	plugin;

	public PaginatedCoreCommand(InfiniteClaims plugin)
	{
		super(plugin);
		this.plugin = plugin;
	}

}
