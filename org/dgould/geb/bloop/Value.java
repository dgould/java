package org.dgould.geb.bloop;
import org.dgould.*;

public class Value extends Object
{
	public Boolean boolValue;
	public NaturalNumber numberValue;
	
	public Value()
	{
		this.boolValue = null;
		this.numberValue = null;
	}
	
	public Value(Boolean boolValue)
	{
		this.boolValue = boolValue;
	}
	
	public Value(NaturalNumber numberValue)
	{
		this.numberValue = numberValue;
	}
	
	public String toString()
	{
		if(this.IsBoolean())
		{
			return this.boolValue.booleanValue() ? "YES" : "NO";
		}
		return this.numberValue.FormatToString(10);
	}
	
	public boolean IsBoolean()
	{
		return this.boolValue != null;
	}
	
	public boolean IsNumber()
	{
		return this.numberValue != null;
	}
}
