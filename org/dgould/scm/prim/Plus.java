/* Plus.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm.prim;

import org.dgould.scm.*;


public class Plus extends Primitive
{
//PRIVATE INSTANCE MEMBERS
	SchemeObject a;
	SchemeObject b;
	
//CONSTRUCTORS
	public Plus()
	{
		a = new SchemeObject(SchemeObject.SYMBOL, (Object)"a");
		b = new SchemeObject(SchemeObject.SYMBOL, (Object)"b");
		parameters = Pair.Cons(a, Pair.Cons(b, SchemeObject.NIL));
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
		SchemeObject bVal = localEnv.GetBinding(b);
		Double result = new Double(
			((Double)(aVal.mContents)).doubleValue() +
			((Double)(bVal.mContents)).doubleValue());
		return new SchemeObject(SchemeObject.NUMBER, (Object)result);
	}
};

