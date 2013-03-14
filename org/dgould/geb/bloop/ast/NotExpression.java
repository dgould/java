package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public class NotExpression extends OperatorExpression
{
	private Expression parameter;
	
	public NotExpression(Expression parameter)
	{
		this.evaluatesToBoolean = true;
		
		this.parameter = parameter;
		
		this.debugStr = "NotExpression\n";
	}

	public void Print(java.io.PrintStream output, String indent)
	{
		output.println(indent + "NotExpression");
		output.println(indent + tab + "parameter:");
		this.parameter.Print(output, indent + tab);
	}
	
	public void StaticSemanticsSelf(BlooPInterpreter interpreter, java.util.Stack context)
		throws SemanticException
	{
		this.parameter.StaticSemantics(interpreter, context);

		if(!this.parameter.IsBoolean())
		{
			throw new SemanticException("non-boolean right operand to 'NOT' operator");
		}
	}
	
	public Value Evaluate() throws LogicException
	{
		Value v = this.parameter.Evaluate();
		
		if(v == null)
		{
			return null;
		}
		
		if(v.IsBoolean())
		{
			return new Value(new Boolean(!v.boolValue.booleanValue()));
		}
		
		throw new LogicException("non-boolean operand to 'NOT' operator");
	}
}
