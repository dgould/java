/*34567890123456789012345678901234567890123456789012345678901234567890123456*/

package org.dgould;

import java.lang.*;
import java.io.*;

public class PeekableReader extends Object
{
	private Reader  reader;
	private int     buffer;
	private boolean hasBuffer;
	private boolean useExceptions;
	
	public PeekableReader(Reader reader, boolean useExceptions)
	{
		this.reader = reader;
		this.buffer = 0;
		this.hasBuffer = false;
		this.useExceptions = useExceptions;
	}
	public PeekableReader(Reader reader)
	{
		this(reader, false);
	}
	
	public boolean UsingExceptions() 
	{
		return useExceptions;
	}
	public void SetExceptions(boolean useExceptions)
	{
		this.useExceptions = useExceptions;
	}
	public void UseExceptions() 
	{
		SetExceptions(true);
	}
	public void DontUseExceptions() 
	{
		SetExceptions(false);
	}
	
	public int read() throws IOException
	{
		int c;
		IOException ex = null;
		if(hasBuffer)
		{
			hasBuffer = false;
			c = buffer;
		}
		else
		{
			try
			{
				c = reader.read();
			}
			catch(IOException e)
			{
				c = -1;
				ex = e;
			}
		}
		if(useExceptions && (c < 0))
		{
			throw ((ex != null) ? ex : (new IOException("EOF")));
		}
		return c;
	}
	
	public int peek()
	{
		if(hasBuffer)
		{
			return buffer;
		}
		try
		{
			buffer = reader.read();
		}
		catch(IOException e)
		{
			buffer = -1;
		}
		hasBuffer = true;
		return buffer;
	}
}