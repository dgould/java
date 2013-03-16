/* Assoc.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm.prim;

import org.dgould.scm.*;


public class Assoc extends Primitive
{
//PRIVATE INSTANCE MEMBERS
	SchemeObject item;
	SchemeObject list;
	
//CONSTRUCTORS
	public Assoc()
	{
		item = new SchemeObject(SchemeObject.SYMBOL, (Object)"item");
		list = new SchemeObject(SchemeObject.SYMBOL, (Object)"list");
		parameters = Pair.Cons(item, Pair.Cons(list, SchemeObject.NIL));
	}
	
	public SchemeObject Invoke(SchemeObject args, SchemeEnvironment env)
	{
		SchemeEnvironment localEnv = BindArgs(args, env);
		if(localEnv == null)
		{
			return new SchemeObject(SchemeObject.ERROR,
				(Object)"Wrong number of arguments");
		}
		SchemeObject itemVal = localEnv.GetBinding(item);
		SchemeObject listVal = localEnv.GetBinding(list);
		
		while(listVal != SchemeObject.NIL)
		{
			if(SchemeObject.StructuresEqual(itemVal, listVal.Car().Car()))
			{
				return listVal.Car();
			}
			listVal = listVal.Cdr();
		}
		
		return listVal;
	}
}

