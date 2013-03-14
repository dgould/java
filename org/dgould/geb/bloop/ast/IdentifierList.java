package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public class IdentifierList extends Ast
{
	protected String car;
	protected IdentifierList cdr;
	
	public IdentifierList(String car, IdentifierList cdr)
	{
		this.car = car;
		this.cdr = cdr;
		
		this.debugStr = "IdentifierList\n";
	}

	public void Print(java.io.PrintStream output, String indent)
	{
		output.println(indent + "IdentifierList");
		IdentifierList temp = this;
		while(temp != null)
		{
			output.println(indent + tab + temp.car);
			temp = temp.cdr;
		}
	}
	
	public void StaticSemanticsSelf(BlooPInterpreter interpreter, java.util.Stack context)
		throws SemanticException
	{
		if(this.cdr != null)
		{
			this.cdr.StaticSemantics(interpreter, context);
		}
	}
	
	public int Count()
	{
		if(this.cdr == null)
		{
			return 1;
		}
		return 1 + this.cdr.Count();
	}
}
