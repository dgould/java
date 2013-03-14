package org.dgould.geb.bloop;
import org.dgould.*;

public class BlockExitChain extends Exception
{
	public NaturalNumber blockNum;
	public boolean forLoop;
	public boolean reachedLoop;
	
	public BlockExitChain(NaturalNumber blockNum, boolean forLoop)
	{
		this.blockNum = blockNum;
		this.forLoop  = forLoop;
		this.reachedLoop = false;
	}
}
