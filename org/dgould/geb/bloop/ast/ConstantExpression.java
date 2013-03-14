package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public class ConstantExpression extends Expression
{
	private Value value;
	
	public ConstantExpression(Value value)
	{
		this.evaluatesToBoolean = value.IsBoolean();
		
		this.value = value;
		
		this.debugStr = "ConstantExpression: " + value.toString() + "\n";
	}

	public void Print(java.io.PrintStream output, String indent)
	{
		output.println(indent + "ConstantExpression: \"" + this.value.toString() + "\"");
	}
	
	public void StaticSemanticsSelf(BlooPInterpreter interpreter, java.util.Stack context)
		throws SemanticException
	{
	}
	
	public Value Evaluate() throws LogicException
	{
		return this.value;
	}
}
