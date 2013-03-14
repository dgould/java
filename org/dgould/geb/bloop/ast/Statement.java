package org.dgould.geb.bloop.ast;
import  org.dgould.geb.bloop.*;
import  org.dgould.*;

public abstract class Statement extends Ast
{
	public abstract void Execute() throws LogicException, BlockExitChain;
}
