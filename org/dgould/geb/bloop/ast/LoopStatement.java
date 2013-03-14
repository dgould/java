package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public class LoopStatement extends Statement
{
	protected Expression limit;
	protected Statement body;
	protected boolean saysAtMost;
	
	public LoopStatement(Expression limit, Statement body, boolean saysAtMost)
	{
		this.limit = limit;
		this.body = body;
		this.saysAtMost = saysAtMost;
		
		this.debugStr = "LoopStatement\n";
	}

	public void Print(java.io.PrintStream output, String indent)
	{
		output.println(indent + "LoopStatement" + (this.saysAtMost ? " with \"AT MOST\"" : ""));
		output.println(indent + tab + "limit:");
		this.limit.Print(output, indent + tab);
		output.println(indent + tab + "body:");
		this.body.Print(output, indent + tab);
	}
	
	public void StaticSemanticsSelf(BlooPInterpreter interpreter, java.util.Stack context)
		throws SemanticException
	{
		context.push(this);
		this.limit.StaticSemantics(interpreter, context);
		this.body.StaticSemantics(interpreter, context);
		context.pop();
	}
	
	public void Execute() throws LogicException, BlockExitChain
	{
		Value value;
		NaturalNumber max;
		
		value = this.limit.Evaluate();
		if(value == null || !value.IsNumber())
		{
			throw new LogicException("invalid limit expression in 'LOOP' statement");
		}
		max = value.numberValue;
		
		try
		{
			for(NaturalNumber i = new NaturalNumber(0);
			    NaturalNumber.Compare(i, max) < 0;
			    i.AddOneDigit(1))
			{
				this.body.Execute();
			}
		}
		catch(BlockExitChain exit)
		{
			if(!exit.reachedLoop)
			{
				throw exit;
			}
		}
	}
}
