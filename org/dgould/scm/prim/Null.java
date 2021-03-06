/* Null.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm.prim;

import org.dgould.scm.*;


// null? takes a single argument and returns true if it is null, else false
public class Null extends Primitive
{
//PRIVATE INSTANCE MEMBERS
	SchemeObject a;
	
//CONSTRUCTORS
	public Null()
	{
		a = new SchemeObject(SchemeObject.SYMBOL, (Object)"a");
		parameters = Pair.Cons(a, SchemeObject.NIL);
	}
	
	public SchemeObject Invoke(SchemeObject args, SchemeEnvironment env)
	{
		SchemeEnvironment localEnv = BindArgs(args, env);
		if(localEnv == null)
		{
			return new SchemeObject(SchemeObject.ERROR,
				(Object)"Wrong number of arguments");
		}
		SchemeObject aVal = localEnv.GetBinding(a);

		if(aVal == SchemeObject.NIL)
		{
			return SchemeObject.TRUE;
		}
		return SchemeObject.NIL;
	}
}

