package com.hskrasek.InfiniteClaims;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

@Deprecated
public class InfiniteClaimsLogger extends Logger
{
	FileHandler	fileHandler;
	Logger		log		= null;
	String		prefix	= "[InfiniteClaims] ";

	public InfiniteClaimsLogger(String logger, String file)
	{
		super(logger, null);

		try
		{
			fileHandler = new FileHandler(file, true);
			this.setUseParentHandlers(false);
			List<Handler> handlers = Arrays.asList(this.getHandlers());
			for (Handler handler : handlers)
			{
				this.removeHandler(handler);
			}
			this.addHandler(fileHandler);
			this.setLevel(Level.ALL);
			this.fileHandler.setFormatter(new LogFormatter());
		}
		catch (SecurityException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void setPrefix(String prefix)
	{
		this.prefix = prefix + " ";
	}

	public void setDefaultLogger(Logger logger)
	{
		this.log = logger;
	}

	public void log(Level level, String message)
	{
		if ((level.intValue() < Level.WARNING.intValue()) && (log != null))
		{
			log.log(level, prefix + message);
		}

		super.log(level, prefix + message);
	}

	public void close()
	{
		fileHandler.close();
	}

	private class LogFormatter extends Formatter
	{
		private final SimpleDateFormat	date	= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		public String format(LogRecord record)
		{
			StringBuilder builder = new StringBuilder();
			Throwable error = record.getThrown();

			builder.append(this.date.format(record.getMillis()));
			builder.append(" [" + record.getLevel().getLocalizedName().toUpperCase());
			builder.append("] " + record.getMessage());
			builder.append("\n");

			if (error != null)
			{
				StringWriter errOut = new StringWriter();
				error.printStackTrace(new PrintWriter(errOut));
				builder.append(errOut);
			}
			return builder.toString();
		}
	}
}
