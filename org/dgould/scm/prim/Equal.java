/* Equal.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm.prim;

import org.dgould.scm.*;


//equal takes two arguments and return true if they evaluate to equal values
//(not necessarily the same object), else false
public class Equal extends Primitive
{
//PRIVATE INSTANCE MEMBERS
	SchemeObject a;
	SchemeObject b;
	
//CONSTRUCTORS
	public Equal()
	{
		a = new SchemeObject(SchemeObject.SYMBOL, (Object)"a");
		b = new SchemeObject(SchemeObject.SYMBOL, (Object)"b");
		parameters = Pair.Cons(a, Pair.Cons(b, SchemeObject.NIL));
	}
	
	//same as Eq.Invoke(), plus case to compare lists recursively
	//this WILL crash on circular lists
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
		
		if(SchemeObject.StructuresEqual(aVal, bVal))
		{
			return SchemeObject.TRUE;
		}
		return SchemeObject.NIL;
	}
}

