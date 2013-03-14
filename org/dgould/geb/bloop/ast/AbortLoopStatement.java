package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public class AbortLoopStatement extends Statement
{
	private NaturalNumber loopNum;
	
	public AbortLoopStatement(NaturalNumber loopNum)
	{
		this.loopNum = loopNum;
		
		this.debugStr = "AbortLoopStatement\n";
	}

	public void Print(java.io.PrintStream output, String indent)
	{
		output.println(indent + "AbortLoopStatement for Block #" + this.loopNum.toString());
	}
	
	public void StaticSemanticsSelf(BlooPInterpreter interpreter, java.util.Stack context)
		throws SemanticException
	{
		for(int i = context.size() - 1; i >= 0; i--)
		{
			Object x = context.elementAt(i);
			if(x instanceof Block)
			{
				if(NaturalNumber.Compare(((Block)x).blockNum, this.loopNum) == 0)
				{
					if(context.elementAt(i - 1) instanceof LoopStatement)
					{
						if(((LoopStatement)context.elementAt(i - 1)).saysAtMost)
						{
							return;
						}
						throw new SemanticException("Loop " + this.loopNum.toString() +
						                            " has an 'ABORT LOOP' statement, " +
						                            "but no 'AT MOST' clause");
					}
					throw new SemanticException("Block " + this.loopNum.toString() + 
					                            " is not a loop body, but has an " + 
					                            "'ABORT LOOP' clause");
				}
			}
		}
		throw new SemanticException("invalid block number " + 
		                            this.loopNum.toString() +
		                            " in 'ABORT LOOP' statement");
	}
	
	public void Execute() throws LogicException, BlockExitChain
	{
		throw new BlockExitChain(this.loopNum, true);
	}
}
