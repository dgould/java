package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public class QuitBlockStatement extends Statement
{
	private NaturalNumber blockNum;
	
	public QuitBlockStatement(NaturalNumber blockNum)
	{
		this.blockNum = blockNum;
		
		this.debugStr = "QuitBlockStatement\n";
	}

	public void Print(java.io.PrintStream output, String indent)
	{
		output.println(indent + "QuitBlockStatement for Block #" + this.blockNum.toString());
	}
	
	public void StaticSemanticsSelf(BlooPInterpreter interpreter, java.util.Stack context)
		throws SemanticException
	{
		for(int i = context.size() - 1; i >= 0; i--)
		{
			Object x = context.elementAt(i);
			if(x instanceof Block)
			{
				if(NaturalNumber.Compare(((Block)x).blockNum, this.blockNum) == 0)
				{
					return;
				}
			}
		}
		throw new SemanticException("invalid block number " + 
		                            this.blockNum.toString() +
		                            " in 'QUIT BLOCK' statement");
	}
	
	public void Execute() throws LogicException, BlockExitChain
	{
		throw new BlockExitChain(this.blockNum, false);
	}
}
