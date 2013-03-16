/* Random.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm.prim;

import org.dgould.scm.*;


public class Random extends Primitive
{
//PRIVATE INSTANCE MEMBERS
	SchemeObject a;
	java.util.Random r;
	
//CONSTRUCTORS
	public Random()
	{
		a = new SchemeObject(SchemeObject.SYMBOL, (Object)"a");
		parameters = Pair.Cons(a, SchemeObject.NIL);
		r = new java.util.Random();
	}
	public Random(long seed)
	{
		a = new SchemeObject(SchemeObject.SYMBOL, (Object)"a");
		parameters = Pair.Cons(a, SchemeObject.NIL);
		r = new java.util.Random(seed);
	}

//PUBLIC INSTANCE METHODS
	public SchemeObject Invoke(SchemeObject args, SchemeEnvironment env)
	{		
		SchemeEnvironment localEnv = BindArgs(args, env);
		if(localEnv == null)
		{
			return new SchemeObject(SchemeObject.ERROR,
				(Object)"Wrong number of arguments");
		}
		
		SchemeObject aVal = localEnv.GetBinding(a);
		if(!aVal.isNumber())
		{
			return new SchemeObject(SchemeObject.ERROR,
				(Object)"argument to random is not a number");
		}
		
		int max = ((Double)(aVal.mContents)).intValue();
		if(max == 0)
		{
			return new SchemeObject(SchemeObject.NUMBER, new Double(0));
		}
		long n = r.nextLong();
		if(n < 0) {n *= -1;}
		return new SchemeObject(SchemeObject.NUMBER, new Double((double)(n % max)));
	}
};

