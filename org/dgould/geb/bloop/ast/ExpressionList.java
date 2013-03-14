package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public class ExpressionList extends Ast
{
	protected Expression car;
	protected ExpressionList cdr;
	
	public ExpressionList(Expression car, ExpressionList cdr)
	{
		this.car = car;
		this.cdr = cdr;
		this.debugStr = "ExpressionList\n";
	}

	public void Print(java.io.PrintStream output, String indent)
	{
		output.println(indent + "ExpressionList");
		ExpressionList temp = this;
		while(temp != null)
		{
			temp.car.Print(output, indent + tab);
			temp = temp.cdr;
		}
	}
	
	public void StaticSemanticsSelf(BlooPInterpreter interpreter, java.util.Stack context)
		throws SemanticException
	{
		this.car.StaticSemantics(interpreter, context);
		
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
