package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public class AssignmentStatement extends Statement
{
	protected Expression cellNum;
	protected Expression value;
	
	protected ProcedureDefinition scope;
	
	public AssignmentStatement(Expression cellNum, Expression value)
	{
		this.cellNum = cellNum;
		this.value = value;
		
		this.debugStr = "AssignmentStatement\n";
	}

	public void Print(java.io.PrintStream output, String indent)
	{
		output.println(indent + "AssignmentStatement");
		if(this.cellNum == null)
		{
			output.println(indent + tab + "assigning OUTPUT");
		}
		else
		{
			output.println(indent + tab + "cellNum:");
			this.cellNum.Print(output, indent + tab);
		}
		output.println(indent + tab + "value:");
		this.value.Print(output, indent + tab);
	}
	
	public void StaticSemanticsSelf(BlooPInterpreter interpreter, java.util.Stack context)
		throws SemanticException
	{
		this.scope = (ProcedureDefinition)(context.elementAt(0));
		
		if(this.cellNum != null)
		{
			this.cellNum.StaticSemantics(interpreter, context);
		}
		this.value.StaticSemantics(interpreter, context);
		
		if(this.cellNum == null)
		{
			if(this.scope.IsBoolean() && this.value.IsNumber())
			{
				throw new SemanticException("numeric return value for a Boolean procedure");
			}
			if(this.scope.IsNumber() && this.value.IsBoolean())
			{
				throw new SemanticException("Boolean return value for a numeric procedure");
			}
		}
		else
		{			
			if(!this.value.IsNumber())
			{
				throw new SemanticException("assigning a non-numeric value to CELL(" + 
					this.cellNum.toString() + ")");
			}
		}
	}
	
	public void Execute() throws LogicException, BlockExitChain
	{
		Value newValue = this.value.Evaluate();
		if(newValue == null)
		{
			throw new LogicException("invalid new value in assignment statement");
		}
		
		if(this.cellNum == null)
		{
			this.scope.environment.SetOutput(newValue);
		}
		else
		{
			Value cell = this.cellNum.Evaluate();
			if(cell == null || !cell.IsNumber())
			{
				throw new LogicException("invalid subscript in CELL assignment");
			}
			
			this.scope.environment.SetCell(cell.numberValue, newValue);
		}
	}
}
