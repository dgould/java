package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public class EqualsExpression extends OperatorExpression
{
	private Expression left;
	private Expression right;
	
	public EqualsExpression(Expression left, Expression right)
	{
		this.evaluatesToBoolean = true;
		
		this.left = left;
		this.right = right;
		
		this.debugStr = "Expression\n";
	}

	public void Print(java.io.PrintStream output, String indent)
	{
		output.println(indent + "EqualsExpression");
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
		
		if(!((this.left.IsNumber()  && this.right.IsNumber()) ||
		     (this.left.IsBoolean() && this.right.IsBoolean())))
		{
			throw new SemanticException("non-matching operand types to '=' operator");
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
		
		if(v1.IsNumber() && v2.IsNumber())
		{
			return new Value(new Boolean(NaturalNumber.Compare(v1.numberValue,
			                                                   v2.numberValue) == 0));
		}
		
		if(v1.IsBoolean() && v2.IsBoolean())
		{
			return new Value(new Boolean(v1.boolValue.booleanValue() ==
			                             v2.boolValue.booleanValue()));
		}
		
		throw new LogicException("non-matching operand types to '=' operator");
	}
}
