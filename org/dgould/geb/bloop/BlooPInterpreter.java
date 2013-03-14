package org.dgould.geb.bloop;

import org.dgould.geb.bloop.ast.*;
import org.dgould.*;

public class BlooPInterpreter extends Object
{
	protected java.util.Hashtable	procedures;
	public NaturalNumber			numDefinitions;
	public boolean debug;
	
	public BlooPInterpreter()
	{
		this.Reset();
		this.debug = false;
	}
	
	public void DefineProcedure(ProcedureDefinition proc) throws SemanticException, LogicException
	{
		this.procedures.put(proc.name, proc);
		proc.StaticSemantics(this, null);
		numDefinitions.AddOneDigit(1);
	}

	public ProcedureDefinition GetProcedure(String name)
	{
		return (ProcedureDefinition)(this.procedures.get(name));
	}
	
	public Value EvaluateCall(CallExpression call) throws SemanticException, LogicException
	{
		call.StaticSemantics(this, null);
		return call.Evaluate();
	}
	
	public void Reset()
	{
		this.procedures = new java.util.Hashtable();
		this.numDefinitions = new NaturalNumber(0);
	}
}
