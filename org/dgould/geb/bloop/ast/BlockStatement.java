package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public class BlockStatement extends Statement
{
	private Block body;
	
	public BlockStatement(Block body)
	{
		this.body = body;
		
		this.debugStr = "BlockStatement\n";
	}

	public void Print(java.io.PrintStream output, String indent)
	{
		output.println(indent + "BlockStatement");
		this.body.Print(output, indent);
	}
	
	public void StaticSemanticsSelf(BlooPInterpreter interpreter, java.util.Stack context)
		throws SemanticException
	{
		this.body.StaticSemantics(interpreter, context);
	}
	
	public void Execute() throws LogicException, BlockExitChain
	{
		StatementList statements = this.body.statements;
		
		try
		{
			while(statements != null)
			{
				statements.car.Execute();
				statements = statements.cdr;
			}
		}
		catch(BlockExitChain exit)
		{
			if(NaturalNumber.Compare(exit.blockNum, this.body.blockNum) != 0)
			{
				throw exit;
			}
			if(exit.forLoop)
			{
				exit.reachedLoop = true;
				throw exit;
			}
		}
	}
}
