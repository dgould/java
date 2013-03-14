package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public class CallExpression extends Expression
{
	protected String procedureName;
	protected ExpressionList actualArguments;
	
	protected ProcedureDefinition procedure;
	
	public CallExpression(String procedureName, ExpressionList actualArguments)
	{
		this.actualArguments = actualArguments;
		this.procedureName = procedureName;
		
		this.debugStr = "CallExpression for '" + procedureName + "'\n";
	}

	public void Print(java.io.PrintStream output, String indent)
	{
		output.println(indent + "CallExpression for procedure \"" + this.procedureName + "\"");
		output.println(indent + tab + "actualArguments:");
		this.actualArguments.Print(output, indent + tab);
	}
	
	public void StaticSemanticsSelf(BlooPInterpreter interpreter, java.util.Stack context)
		throws SemanticException
	{
		this.procedure = interpreter.GetProcedure(this.procedureName);
		
		if(this.procedure == null)
		{
			throw new SemanticException("call to undefined procedure '" +
			                            this.procedureName +
			                            "'");
		}
		
		//Forward reference is not allowed, including through such tricks as redefining
		//a procedure -- if a procedure calls another procedure, the caller's definition
		//must be strictly newer than the callee's.
		if(context != null && context.elementAt(0) instanceof ProcedureDefinition)
		{
			ProcedureDefinition caller = (ProcedureDefinition)(context.elementAt(0));
			
			if(NaturalNumber.Compare(this.procedure.ordinality, caller.ordinality) >= 0)
			{
				throw new SemanticException(
				    "recursive or forward-referring call to procedure \"" +
				    this.procedureName +
				    "\" from procedure \"" +
				    caller.name +
				    "\"");
			}
		}
		
		this.evaluatesToBoolean = this.procedure.IsBoolean();
		
		if(this.actualArguments.Count() != this.procedure.formalParameters.Count())
		{
			throw new SemanticException("wrong number of arguments in call to procedure \"" +
			                            this.procedureName +
			                            "\"");
		}
		
		this.actualArguments.StaticSemantics(interpreter, context);
	}
	
	public Value Evaluate() throws LogicException
	{
		return this.procedure.Apply(this.actualArguments);
	}
}
