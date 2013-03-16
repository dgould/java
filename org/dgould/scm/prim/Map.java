/* Map.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm.prim;

import org.dgould.scm.*;


public class Map extends Primitive
{
//PRIVATE INSTANCE MEMBERS
	SchemeObject proc;
	SchemeObject list;
	
//CONSTRUCTORS
	public Map()
	{
		proc = new SchemeObject(SchemeObject.SYMBOL, (Object)"proc");
		list = new SchemeObject(SchemeObject.SYMBOL, (Object)"list");
		parameters = Pair.Cons(proc, Pair.Cons(list, SchemeObject.NIL));
	}
	
	public SchemeObject Invoke(SchemeObject args, SchemeEnvironment env)
	{
		SchemeEnvironment localEnv = BindArgs(args, env);
		if(localEnv == null)
		{
			return new SchemeObject(SchemeObject.ERROR,
				(Object)"Wrong number of arguments");
		}
		SchemeObject procVal = localEnv.GetBinding(proc);
		SchemeObject listVal = localEnv.GetBinding(list);
		
		return Map(procVal, listVal, env);
	}
	
	public static SchemeObject Map(SchemeObject proc, SchemeObject list,
		SchemeEnvironment env)
	{
		if(list == SchemeObject.NIL)
		{
			return SchemeObject.NIL;
		}
		if(!list.isPair())
		{
			return new SchemeObject(SchemeObject.ERROR, "pair expected in map");
		}
		return Pair.Cons(((Procedure)(proc.mContents)).Apply(
		                     Pair.Cons(Pair.Cons(new SchemeObject(SchemeObject.SYMBOL,
		                                                          SchemeInterpreter.SPECIAL_QUOTE),
		                                         Pair.Cons(list.Car(), SchemeObject.NIL)),
		                               SchemeObject.NIL), env),
		                 Map(proc, list.Cdr(), env));
	}
}

