package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public class Program extends Ast
{
	public ProcedureDefinition firstProc;
	public Ast theRest;
	
	public Program(ProcedureDefinition firstProc, Ast theRest)
	{
		this.firstProc = firstProc;
		this.theRest = theRest;
		this.debugStr = "Program\n";
	}

	public void Print(java.io.PrintStream output, String indent)
	{
		output.println(indent + "Program");
		Object temp = this;
		while(temp != null)
		{
			if(temp instanceof Program)
			{
				((Program)temp).firstProc.Print(output, indent + tab);
				temp = ((Program)temp).theRest;
			}
			else
			{
				((CallExpression)temp).Print(output, indent + tab);
				temp = null;
			}
		}
	}
	
	public void StaticSemanticsSelf(BlooPInterpreter interpreter, java.util.Stack context)
		throws SemanticException
	{
		this.firstProc.StaticSemantics(interpreter, context);
		this.theRest.StaticSemantics(interpreter, context);
	}
}
