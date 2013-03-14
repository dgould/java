package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public class CellExpression extends Expression
{
	protected Expression index;
	protected ProcedureDefinition scope;
	
	public CellExpression(Expression index)
	{
		this.evaluatesToBoolean = false;
		
		this.index = index;
		
		this.debugStr = "CellExpression\n";
	}

	public void Print(java.io.PrintStream output, String indent)
	{
		output.println(indent + "CellExpression");
		output.println(indent + tab + "index:");
		this.index.Print(output, indent + tab);
	}
	
	public void StaticSemanticsSelf(BlooPInterpreter interpreter, java.util.Stack context)
		throws SemanticException
	{
		this.scope = (ProcedureDefinition)(context.elementAt(0));
		
		this.index.StaticSemantics(interpreter, context);
	}
	
	public Value Evaluate() throws LogicException
	{
		Value index = this.index.Evaluate();
		
		if(index == null || !(index.IsNumber()))
		{
			throw new LogicException("invalid subscript in CELL expression");
		}
		
		Value value = this.scope.environment.GetCell(index.numberValue);
		if(value == null)
		{
			throw new LogicException("NULL value in CELL expression");
		}
		return value;
	}
}
