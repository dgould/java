package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public class OrExpression extends OperatorExpression
{
	private Expression left;
	private Expression right;
	
	public OrExpression(Expression left, Expression right)
	{
		this.evaluatesToBoolean = true;
		
		this.left = left;
		this.right = right;
		
		this.debugStr = "OrExpression\n";
	}

	public void Print(java.io.PrintStream output, String indent)
	{
		output.println(indent + "OrExpression");
		output.println(indent + tab + "left:");
		this.left.Print(output, indent + tab);
		output.println(indent + tab + "right:");
		this.right.Print(output, indent + tab);
	}
	
	public void StaticSemanticsSelf(BlooPInterpreter interpreter, java.util.Stack context)
		throws SemanticException
	{
		this.left.StaticSemantics(interpreter, context);
		this.right.StaticSemantics(interpreter, context);

		if(!this.left.IsBoolean())
		{
			throw new SemanticException("non-numeric left operand to 'OR' operator");
		}
		if(!this.right.IsBoolean())
		{
			throw new SemanticException("non-numeric right operand to 'OR' operator");
		}
	}
	
	public Value Evaluate() throws LogicException
	{
		Value v1 = this.left.Evaluate();
		Value v2 = this.right.Evaluate();
		
		if(v1 == null || v2 == null)
		{
			return null;
		}
		
		if(v1.IsBoolean() && v2.IsBoolean())
		{
			return new Value(new Boolean(v1.boolValue.booleanValue() ||
			                             v2.boolValue.booleanValue()));
		}
		
		throw new LogicException("non-boolean operand to 'OR' operator");
	}
}
