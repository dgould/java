/* ListRef.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm.prim;

import org.dgould.scm.*;


public class ListRef extends Primitive
{
//PRIVATE INSTANCE MEMBERS
	SchemeObject l;
	SchemeObject n;
	
//CONSTRUCTORS
	public ListRef()
	{
		l = new SchemeObject(SchemeObject.SYMBOL, (Object)"l");
		n = new SchemeObject(SchemeObject.SYMBOL, (Object)"n");
		parameters = Pair.Cons(l, Pair.Cons(n, SchemeObject.NIL));
	}
	
	public SchemeObject Invoke(SchemeObject args, SchemeEnvironment env)
	{
		SchemeEnvironment localEnv = BindArgs(args, env);
		if(localEnv == null)
		{
			return new SchemeObject(SchemeObject.ERROR,
				(Object)"Wrong number of arguments");
		}
		SchemeObject lVal = localEnv.GetBinding(l);
		SchemeObject nVal = localEnv.GetBinding(n);
		
		int index = ((Double)(nVal.mContents)).intValue();
		SchemeObject list = lVal;
		while(index > 0)
		{
			if(!list.isPair())
			{
				return new SchemeObject(SchemeObject.ERROR,
					(Object)"pair expected in list-ref");
			}
			list = list.Cdr();
			index--;
		}
		if(!list.isPair())
		{
			return new SchemeObject(SchemeObject.ERROR,
				(Object)"pair expected in list-ref");
		}
		return list.Car();
	}
}
