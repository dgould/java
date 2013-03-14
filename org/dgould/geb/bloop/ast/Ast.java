package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public abstract class Ast
{
	protected static final String tab = "| ";
	
	protected BlooPInterpreter interpreter;
	protected String debugStr = "";
	
	public abstract void Print(java.io.PrintStream output, String indent);
	public void Print(java.io.PrintStream output)
	{
		this.Print(output, "");
	}
	
	public final void StaticSemantics(BlooPInterpreter interpreter,
	                                  java.util.Stack context)
		throws SemanticException
	{
		this.interpreter = interpreter;
		
		if(interpreter.debug)
		{
			this.DebugSemantics(System.out);
		}
		
		this.StaticSemanticsSelf(interpreter, context);
	}
	
	public void DebugSemantics(java.io.PrintStream output)
	{
		output.print(this.debugStr);
	}
	
	public abstract void StaticSemanticsSelf(BlooPInterpreter interpreter,
	                                         java.util.Stack context)
		throws SemanticException;
}
