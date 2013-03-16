/* Cadr.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm.prim;

import org.dgould.scm.*;


public class Cadr extends Primitive
{
//PRIVATE INSTANCE MEMBERS
	SchemeObject a;
	SchemeObject b;
	
//CONSTRUCTORS
	public Cadr()
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
		
		if(!aVal.isPair() || !aVal.Cdr().isPair())
		{
			return new SchemeObject(SchemeObject.ERROR,
				(Object)"pair expected in caddr");
		}
		
		return aVal.Cdr().Car();
	}
}

