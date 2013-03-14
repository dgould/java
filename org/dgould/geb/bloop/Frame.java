package org.dgould.geb.bloop;
import org.dgould.*;

public class Frame extends Object
{
	protected java.util.Hashtable	variables;
	protected java.util.Hashtable	cells;
	
	protected Value					output;
	
	public Frame()
	{
		this.variables	= new java.util.Hashtable();
		this.cells		= new java.util.Hashtable();
		this.output		= null;
	}
	
	public void SetOutput(Value value)
	{
		this.output = value;
	}
	
	public Value GetOutput()
	{
		return this.output;
	}
	
	public void SetCell(NaturalNumber index, Value value)
	{
		this.cells.put(index, value);
	}
	
	public Value GetCell(NaturalNumber index)
	{
		return (Value)(this.cells.get(index));
	}
	
	public void SetVariable(String identifier, Value value)
	{
		/*System.out.println("SetVariable(" + (identifier == null ? "null" : identifier) + 
			", " + (value == null ? "null" : value.toString()) + ")");*/
		if(value != null)
		{
			this.cells.put(identifier, value);
		}
	}
	
	public Value GetVariable(String identifier)
	{
		return (Value)(this.cells.get(identifier));
	}
}
