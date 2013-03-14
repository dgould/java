package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public class VariableExpression extends Expression
{
	private String identifier;
	
	protected ProcedureDefinition scope;
	
	public VariableExpression(String identifier)
	{
		this.evaluatesToBoolean = false;
		
		this.identifier = identifier;
	}

	public void Print(java.io.PrintStream output, String indent)
	{
		output.println(indent + "VariableExpression: \"" + this.identifier + "\"");
	}
	
	public void DebugSemantics(java.io.PrintStream output)
	{
		output.println("VariableExpression.StaticSemantics(): identifier = '" +
		               this.identifier + "'");
	}
	
	public void StaticSemanticsSelf(BlooPInterpreter interpreter, java.util.Stack context)
		throws SemanticException
	{
		this.scope = (ProcedureDefinition)(context.elementAt(0));
		
		if(!(this.scope.isFormalParameter(this.identifier)))
		{
			throw new SemanticException("invalid identifier '" +
			                            this.identifier +
			                            "' in variable expression");
		}
	}
	
	public Value Evaluate() throws LogicException
	{
		return this.scope.environment.GetVariable(this.identifier);
	}
}
