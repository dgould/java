package org.dgould.geb.bloop;
import org.dgould.*;

public class SemanticException extends Exception
{
	public SemanticException()
	{
		this("");
	}
	
	public SemanticException(String s)
	{
		super("Semantic Error in BlooP program: " + s);
	}
}
