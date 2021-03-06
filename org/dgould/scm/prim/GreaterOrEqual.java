/* GreaterOrEqual.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm.prim;

import org.dgould.scm.*;


//>= takes two number arguments and returns true if the first is >= the second,
//else false
public class GreaterOrEqual extends Primitive
{
//PRIVATE INSTANCE MEMBERS
	SchemeObject a;
	SchemeObject b;
	
//CONSTRUCTORS
	public GreaterOrEqual()
	{
		a = new SchemeObject(SchemeObject.SYMBOL, (Object)"a");
		b = new SchemeObject(SchemeObject.SYMBOL, (Object)"b");
		parameters = Pair.Cons(a, Pair.Cons(b, SchemeObject.NIL));
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
		SchemeObject bVal = localEnv.GetBinding(b);
		
		if(aVal.isNumber() && bVal.isNumber() &&
		   (((Double)(aVal.mContents)).doubleValue() >=
		    ((Double)(bVal.mContents)).doubleValue()))
		{
			return SchemeObject.TRUE;
		}
		return SchemeObject.NIL;
	}
}

