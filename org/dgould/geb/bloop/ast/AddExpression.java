package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public class AddExpression extends OperatorExpression
{
	private Expression left;
	private Expression right;
	
	public AddExpression(Expression left, Expression right)
	{
		this.evaluatesToBoolean = false;
		
		this.left = left;
		this.right = right;
		
		this.debugStr = "AddExpression\n";
	}

	public void Print(java.io.PrintStream output, String indent)
	{
		output.println(indent + "AddExpression");
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

		if(!this.left.IsNumber())
		{
			throw new SemanticException("non-numeric left operand to '+' operator");
		}
		if(!this.right.IsNumber())
		{
			throw new SemanticException("non-numeric right operand to '+' operator");
		}
	}
	
	public Value Evaluate() throws LogicException
	{
		Value v1 = this.left.Evaluate();
		Value v2 = this.right.Evaluate();
		
		if(v1 == null)
		{
			throw new LogicException("NULL left operand to Add expression");
		}
		if(v2 == null)
		{
			throw new LogicException("NULL right operand to Add expression");
		}
		
		if(v1.IsNumber() && v2.IsNumber())
		{
			return new Value(NaturalNumber.Add(v1.numberValue, v2.numberValue));
		}
		
		throw new LogicException("non-number operand to '+' operator");
	}
}
