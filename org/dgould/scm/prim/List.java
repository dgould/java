/* List.java
1234567890123456789012345678901234567890123456789012345678901234567890123456789
*/

package org.dgould.scm.prim;

import org.dgould.scm.*;


//////////////////////////////////////////////////////////////////////////////
// THESE USE NORMAL-ORDER EVALUATION OF ARGUMENTS INSTEAD OF APPLICATIVE ORDER
// I CAN CHEAT LIKE THIS TO MAKE PRIMITIVES THAT ARE REALLY SPECIAL FORMS
// THEY ALSO TAKE VARIABLE NUMBERS OF ARGUMENTS

public class List extends Primitive
{
//CONSTRUCTORS
	public List()
	{
		parameters = null;
	}
	
	public SchemeObject Invoke(SchemeObject args, SchemeEnvironment env)
	{
		SchemeObject argList = args;
		
		if(argList == SchemeObject.NIL)
		{
			return SchemeObject.NIL;
		}
		
		SchemeObject result = Pair.Cons(interpreter.Eval(argList.Car(), env),
		                                SchemeObject.NIL);
		SchemeObject temp = result;
		argList = argList.Cdr();
		
		while(argList != SchemeObject.NIL)
		{
			temp.SetCdr(Pair.Cons(interpreter.Eval(argList.Car(), env),
		                          SchemeObject.NIL));
			if(temp.isError())
			{
				return temp;
			}
			temp = temp.Cdr();
			argList = argList.Cdr();
		}
		return result;
	}
}

