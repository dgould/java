package org.dgould.geb.bloop;
import org.dgould.*;

public class LogicException extends Exception
{
	public LogicException()
	{
		this("");
	}
	
	public LogicException(String s)
	{
		super("Runtime logic Error in BlooP program: " + s);
	}
}
