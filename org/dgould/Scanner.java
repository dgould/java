/*34567890123456789012345678901234567890123456789012345678901234567890123456*/

package org.dgould;

import java.lang.*;
import java.io.*;

public class Scanner extends PeekableReader
{
	public Scanner(Reader reader, boolean useExceptions)
	{
		super(reader, useExceptions);
	}
	public Scanner(Reader reader)
	{
		this(reader, false);
	}
	
	public void EatWhitespace()
	{
		while((peek() > 0) && Character.isWhitespace(this.peek()))
		{
			//this try/catch is provably unnecessary but syntactically required
			try
			{
				read();
			}
			catch(IOException e)
			{
				break;
			}
		}
	}
	
	public String ReadUntilWhitespace()
	{
		String s = "";
		while((peek() > 0) && !Character.isWhitespace(peek()))
		{
			//this try/catch is provably unnecessary but syntactically required
			try
			{
				s = s + (char)read();
			}
			catch(IOException e)
			{
				break;
			}
		}
		return s;
	}
	
	public String ReadUntil(char[] stopList)
	{
		String s = "";
		scanning:
		while(peek() > 0)
		{
			for(int i = 0; i < stopList.length; i++)
			{
				if((char)peek() == stopList[i])
				{
					break scanning;
				}
			}
			//this try/catch is provably unnecessary but syntactically required
			try
			{
				s = s + (char)read();
			}
			catch(IOException e)
			{
				break;
			}
		}
		return s;
	}
	public String ReadUntil(char stopChar)
	{
		String s = "";
		while((peek() > 0) && (peek() != stopChar))
		{
			//this try/catch is provably unnecessary but syntactically required
			try
			{
				s = s + (char)read();
			}
			catch(IOException e)
			{
				break;
			}
		}
		return s;
	}
	public String ReadUntilWhitespaceOr(char[] stopList)
	{
		String s = "";
		scanning:
		while((peek() > 0) && !Character.isWhitespace(peek()))
		{
			for(int i = 0; i < stopList.length; i++)
			{
				if((char)peek() == stopList[i])
				{
					break scanning;
				}
			}
			//this try/catch is provably unnecessary but syntactically required
			try
			{
				s = s + (char)read();
			}
			catch(IOException e)
			{
				break;
			}
		}
		return s;
	}
	public String ReadUntilWhitespaceOr(char stopChar)
	{
		String s = "";
		while((peek() > 0) && (peek() != stopChar) && !Character.isWhitespace(peek()))
		{
			//this try/catch is provably unnecessary but syntactically required
			try
			{
				s = s + (char)read();
			}
			catch(IOException e)
			{
				break;
			}
		}
		return s;
	}
}