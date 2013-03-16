/* Time.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm.prim;

import org.dgould.scm.*;
import java.util.*;


public class Time extends Primitive
{
//PRIVATE INSTANCE MEMBERS
	SchemeObject a;
	
//CONSTRUCTORS
	public Time()
	{
		a = new SchemeObject(SchemeObject.SYMBOL, (Object)"a");
		parameters = Pair.Cons(a, SchemeObject.NIL);
	}

//PUBLIC INSTANCE METHODS
	public SchemeObject Invoke(SchemeObject args, SchemeEnvironment env)
	{
		long start, stop;
		Date timer;
		timer = new Date();
		start = timer.getTime();
		
		SchemeEnvironment localEnv = BindArgs(args, env);
		if(localEnv == null)
		{
			return new SchemeObject(SchemeObject.ERROR,
				(Object)"Wrong number of arguments");
		}
		
		timer = new Date();
		stop = timer.getTime();
		
		double value = (double)stop - start;
		
		System.out.println(Double.toString(value / (double)1000) + " seconds");
		
		SchemeObject aVal = localEnv.GetBinding(a);
		return aVal;
	}
};

