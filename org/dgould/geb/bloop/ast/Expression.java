package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public abstract class Expression extends Ast
{
	protected boolean evaluatesToBoolean;
	
	public abstract Value Evaluate() throws LogicException;
	
	public boolean IsBoolean()
	{
		return this.evaluatesToBoolean;
	}
	
	public boolean IsNumber()
	{
		return !this.IsBoolean();
	}
}
