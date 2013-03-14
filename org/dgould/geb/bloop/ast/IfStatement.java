package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public class IfStatement extends Statement
{
	private Expression testClause;
	private Statement thenClause;
	
	public IfStatement(Expression testClause, Statement thenClause)
	{
		this.testClause = testClause;
		this.thenClause = thenClause;
		
		this.debugStr = "IfStatement\n";
	}

	public void Print(java.io.PrintStream output, String indent)
	{
		output.println(indent + "IfStatement");
		output.println(indent + tab + "testClause:");
		this.testClause.Print(output, indent + tab);
		output.println(indent + tab + "thenClause:");
		this.thenClause.Print(output, indent + tab);
	}
	
	public void StaticSemanticsSelf(BlooPInterpreter interpreter, java.util.Stack context)
		throws SemanticException
	{
		this.testClause.StaticSemantics(interpreter, context);
		this.thenClause.StaticSemantics(interpreter, context);
	}

	public void Execute() throws LogicException, BlockExitChain
	{
		Value test = this.testClause.Evaluate();
		
		if(test == null || !test.IsBoolean())
		{
			throw new LogicException("invalid test clause in IF statement");
		}
		
		if(test.boolValue.booleanValue())
		{
			this.thenClause.Execute();
		}
	}
}
