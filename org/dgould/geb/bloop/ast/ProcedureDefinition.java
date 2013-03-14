package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public class ProcedureDefinition extends Ast
{
	public String name;
	public NaturalNumber ordinality;
	protected Frame environment;
	protected IdentifierList formalParameters;
	protected Statement body;
	
	public ProcedureDefinition(String name, IdentifierList formalParameters, Statement body)
	{
		this.name = name;
		this.formalParameters = formalParameters;
		this.body = body;
		this.ordinality = null;
		
		this.environment = new Frame();
		this.debugStr = "ProcedureDefinition\n";
	}

	public void Print(java.io.PrintStream output, String indent)
	{
		output.println(indent + "ProcedureDefinition \"" + this.name + "\" (" +
		               ((this.ordinality == null) ?
		                "null" :
		                this.ordinality.FormatToString(10)) +
		               ")");
		output.println(indent + tab + "formal parameters:");
		this.formalParameters.Print(output, indent + tab);
		output.println(indent + tab + "body:");
		this.body.Print(output, indent + tab);
	}
	
	public void StaticSemanticsSelf(BlooPInterpreter interpreter, java.util.Stack context)
		throws SemanticException
	{
		this.ordinality = NaturalNumber.Copy(interpreter.numDefinitions);
		
		context = new java.util.Stack();
		context.push(this);
		
			this.formalParameters.StaticSemantics(interpreter, context);
			this.body.StaticSemantics(interpreter, context);
		
		context.pop();
	}
	
	public boolean IsBoolean()
	{
		return this.name.endsWith("?");
	}
	
	public boolean IsNumber()
	{
		return !this.IsBoolean();
	}
	
	protected boolean isFormalParameter(String parameter)
	{
		IdentifierList parameters = this.formalParameters;
		
		while(parameters != null)
		{
			if(parameter.equalsIgnoreCase(parameters.car))
			{
				return true;
			}
			parameters = parameters.cdr;
		}
		return false;
	}
	
	protected Value Apply(ExpressionList actualArguments) throws LogicException
	{
		IdentifierList parameters = this.formalParameters;
		ExpressionList arguments  = actualArguments;
		
		while(parameters != null)
		{
			this.environment.SetVariable(parameters.car, arguments.car.Evaluate());
			
			parameters = parameters.cdr;
			arguments  = arguments.cdr;
		}
		
		try
		{
			this.body.Execute();
		}
		catch(BlockExitChain exit)
		{
			throw new LogicException("HUH? body of procedure '" + this.name + "' threw " +
			                         "a BlockChainExit, which should not be possible");
		}
		
		return this.environment.GetOutput();
	}
}
