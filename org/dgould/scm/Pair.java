/* Pair.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm;

import java.lang.*;
import java.util.*;

public class Pair extends Object
{
//PUBLIC DATA MEMBERS
	public SchemeObject car;
	public SchemeObject cdr;

//CONSTRUCTORS
	public Pair()
	{
		this(null, null);
	}
	public Pair(SchemeObject car, SchemeObject cdr)
	{
		this.car = car;
		this.cdr = cdr;
	}

//PUBLIC CLASS METHODS
	public static SchemeObject Cons(SchemeObject car, SchemeObject cdr)
	{
		Pair p = new Pair(car, cdr);
		SchemeObject obj = new SchemeObject(SchemeObject.PAIR, (Object)p);
		return obj;
	}
}
