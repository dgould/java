package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public class Block extends Ast
{
	protected NaturalNumber blockNum;
	protected StatementList statements;
	
	public Block(NaturalNumber blockNum, StatementList statements)
	{
		this.blockNum = blockNum;
		this.statements = statements;
		
		this.debugStr = "Block\n";
	}

	public void Print(java.io.PrintStream output, String indent)
	{
		output.println(indent + "Block #" + this.blockNum.FormatToString(10));
		output.println(indent + tab + "statements:");
		this.statements.Print(output, indent + tab);
	}
	
	public void StaticSemanticsSelf(BlooPInterpreter interpreter, java.util.Stack context)
		throws SemanticException
	{
		context.push(this);
		this.statements.StaticSemantics(interpreter, context);
		context.pop();
	}
}
